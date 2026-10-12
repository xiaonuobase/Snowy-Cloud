---
name: crypto-sm
description: Snowy 国密体系使用规范：登录密码 SM2 加密传输、口令 SM3 摘要存储、字段级 SM4-CBC 落库加密（CommonSm4CbcTypeHandler）、CommonCryptogramUtil API、前端 smCrypto.js 联动。触发场景：1) 新增敏感字段需要加密存储 2) 处理登录/改密逻辑 3) 前后端加解密联调。触发词：国密、SM2、SM3、SM4、加密、解密、密码、敏感字段、手机号加密、密文、脱敏、CommonCryptogramUtil、TypeHandler、等保。
---

# Snowy 国密体系规范

## 三层国密架构

| 层 | 算法 | 用途 | 实现位置 |
|---|---|---|---|
| 传输 | **SM2**（非对称） | 登录密码前端加密 → 后端解密（防抓包明文） | 前端 `snowy-admin-web/src/utils/smCrypto.js`；后端 `CommonCryptogramUtil`（snowy-base/snowy-common） |
| 摘要 | **SM3**（哈希） | 口令落库摘要（不可逆） | `CommonCryptogramUtil.doHashValue`；`SysPasswordUtil`（sys 插件 sys-func，只在 web-app 里可用）负责口令规则校验 |
| 字段 | **SM4-CBC**（对称） | 手机号/证件号等敏感字段落库加密 | `CommonSm4CbcTypeHandler`（snowy-base/snowy-common 的 common/handler） |

底座：sm-crypto 0.3.2 + BouncyCastle 1.70。软件层面满足等保测评要求。

## CommonCryptogramUtil API（snowy-base/snowy-common 的 common/util）

```java
// SM2 —— 登录密码传输
String cipher = CommonCryptogramUtil.doSm2Encrypt(plain);      // （前端做，后端一般只用解密）
String plain  = CommonCryptogramUtil.doSm2Decrypt(cipher);     // 后端解密前端传来的密码密文

// SM3 —— 摘要
String digest = CommonCryptogramUtil.doHashValue(plain);

// SM4 —— 通用对称加解密（字段加密底层也是它）
String enc = CommonCryptogramUtil.doSm4CbcEncrypt(plain);
String dec = CommonCryptogramUtil.doSm4CbcDecrypt(enc);

// SM2 签名 / 验签
String sign = CommonCryptogramUtil.doSignature(plain);
boolean ok  = CommonCryptogramUtil.doVerifySignature(plain, sign);
```

密钥来源：**不在配置文件里**。SM2 公钥以明文、SM2 私钥与 SM4 key/IV 以异或混淆的字节数组写在 `CommonCryptogramUtil` 源码中，运行时用 `CommonKeyUtil.reconstruct` 还原；前端公钥写在 `smCrypto.js`。生产环境换密钥：运行 `CommonKeyUtil`（`generateSm2KeyPair()` 等）生成新密钥与混淆代码，替换 `CommonCryptogramUtil` 对应数组，同时把新公钥同步到前端 `smCrypto.js`。

> 微服务注意：snowy-common 被网关、web-app、biz-app 共用，换密钥后**所有服务都要重新构建并重启**；各服务必须用同一版本的 snowy-common，否则同一张表的 SM4 字段在不同服务之间解不开（如 biz-app 读写 SYS_USER 的手机号）。

## 字段级 SM4 加密（新增敏感字段的标准做法）

```java
@TableName(value = "BIZ_PATIENT", autoResultMap = true)     // ① 必须 autoResultMap = true
public class BizPatient extends CommonEntity {

    @TableId
    private String id;

    /** 手机号（SM4 加密落库） */
    @TableField(typeHandler = CommonSm4CbcTypeHandler.class)  // ② 加 TypeHandler
    @Schema(description = "手机号")
    private String phone;
}
```

效果：入库自动密文（`insert/update` 生效），查询结果自动解密透明返回。参考实战：`BizUser.phone / idCardNumber / emergencyPhone`（`biz/modular/user/entity/BizUser.java`）。

### ⚠️ SM4 字段的查询限制（必读）

SM4-CBC 用固定 key/IV，同一明文的密文固定，但密文不保留明文的子串关系：**like 模糊查询密文字段查不到任何结果**。处理方案：
- 精确查询：`queryWrapper.lambda().eq(BizPatient::getPhone, 手机号明文)` —— TypeHandler 会让 MP 用密文比对？**不会自动**，需要走 eq 前手动 `CommonCryptogramUtil.doSm4CbcEncrypt(phone)` 加密后比对（以 SysUserServiceImpl / BizUserServiceImpl 现有手机号查询写法为准，模仿之）
- 模糊查询：另存脱敏/哈希辅助列（如 PHONE_HASH 存 SM3 摘要用于查重）
- 列表展示：自动解密无需处理；需要脱敏展示（138****5678）在前端或 Result 层处理

## 登录/改密流程（不要自造）

```
登录：前端 smCrypto.js 用 SM2 公钥加密密码 → 经网关 /api/webapp/auth/b/doLogin 密文上送（服务内路径 /auth/b/doLogin，运行在 web-app）
     → AuthServiceImpl doSm2Decrypt → doHashValue（SM3）后与库中摘要比对
改密/新建用户：sys 插件校验口令规则（SysPasswordUtil）后以 SM3 摘要存储
```

新业务涉及口令（如二级密码）：同样 SM2 传输 + SM3 存储，直接复用 CommonCryptogramUtil（biz-app 没有装 sys-func，用不到 SysPasswordUtil，需要口令规则校验时走 sys 插件的 Api 或在 web-app 侧实现）。

## 前端联动

```js
// snowy-admin-web/src/utils/smCrypto.js
import smCrypto from '@/utils/smCrypto'
const cipher = smCrypto.doSm2Encrypt(密码明文)   // 公钥写死在 smCrypto.js 内，必须与后端 CommonCryptogramUtil.PUBLIC_KEY 一致
```

抓包看到登录接口密码是一长串十六进制密文是**正常的**（长度 = 192 + 2×明文字节数，如 `Snowy@2026!` 为 214 位）。

## 禁止事项

- ❌ 明文存储/传输任何口令、身份证、手机号
- ❌ 自选 MD5/SHA-1/DES/AES（项目标准是国密）
- ❌ 在业务代码里另存/硬编码密钥（密钥只在 `CommonCryptogramUtil` 混淆存放 + 前端 `smCrypto.js` 公钥两处维护）
- ❌ 日志输出明文敏感字段（log 里脱敏或干脆不打）
- ❌ 对 SM4 字段写 like 查询（永远查不到）

## 检查清单

- [ ] 新敏感字段：@TableName(autoResultMap=true) + @TableField(typeHandler=CommonSm4CbcTypeHandler.class)
- [ ] 该字段有精确/模糊查询需求时已评估方案（密文 eq / HASH 辅助列）
- [ ] 口令类只存 SM3 摘要，传输走 SM2
- [ ] 业务代码未另存密钥；换密钥时前后端公钥已同步、所有服务已重新构建
- [ ] 日志无明文敏感信息

## 参考实现

| 文件 | 说明 |
|---|---|
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonCryptogramUtil.java` | 国密工具 API + 混淆存放的密钥 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/util/CommonKeyUtil.java` | 密钥还原与新密钥生成 |
| `snowy-base/snowy-common/src/main/java/vip/xiaonuo/common/handler/CommonSm4CbcTypeHandler.java` | 字段加密 TypeHandler |
| `snowy-plugin/snowy-plugin-biz/snowy-plugin-biz-func/src/main/java/vip/xiaonuo/biz/modular/user/entity/BizUser.java` | SM4 字段实战 |
| `snowy-plugin/snowy-plugin-auth/snowy-plugin-auth-func/src/main/java/vip/xiaonuo/auth/modular/login/service/impl/AuthServiceImpl.java` | SM2 解密 + SM3 校验现场 |
| `snowy-plugin/snowy-plugin-sys/snowy-plugin-sys-func/src/main/java/vip/xiaonuo/sys/core/util/SysPasswordUtil.java` | 口令规则校验（web-app 内） |
| `snowy-admin-web/src/utils/smCrypto.js` | 前端国密（公钥） |
