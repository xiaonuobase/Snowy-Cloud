---
name: git-workflow
description: 本项目 Git 规范：提交四原则、conventional commit 中文格式、仓库状态检测与降级、不提交清单。触发场景：1) 提交代码 2) 用户要求 push/分支操作 3) 检查仓库状态。触发词：Git、提交、commit、push、分支、merge、回退、暂存、仓库。
---

# Git 工作流规范

## 仓库现状

- 本仓库一般克隆自官方 Snowy-Cloud 仓库，`origin` 指向官方远程（以 `git remote -v` 为准），分支含 master、dev 等；当前开发分支以 `git branch --show-current` 为准
- 上游官方提交使用"【底座】/【更新】"等前缀；本项目自己的二次开发提交按下方 Commit Message 规范
- 根目录 `.gitignore` 覆盖 target/、.idea/、logs/、*.log 等；`snowy-admin-web/.gitignore` 覆盖 node_modules/、dist/、.env.local 等

## 提交四原则（硬性）

1. **只提交本次任务相关的文件**——`git status` 逐个确认，禁止 `git add -A` 一把梭（可能带进无关改动）
2. **不提交敏感与本地配置改动**——Nacos 配置源文件（`snowy-server/snowy-nacos-app/src/main/resources/META-INF/config-data/**/*.yaml`）与 `nacos-db*.sql` 里改成本机的数据库/Redis 口令、库号，`snowy-nacos-app` 的 `application.properties` 本地口令，各服务 `application.yml` 的 Nacos 账号，前端 .env 类文件（已忽略的除外也要留意）
3. **默认只做本地 commit**——push 必须用户明确要求；`origin` 若指向官方远程，更不能随手 push
4. **提交前过 /check 或 code-patterns 检查清单**——规范违规不提交

## Commit Message 规范

```
<type>: <中文描述>

type ∈ feat | fix | refactor | docs | style | test | chore | perf
```

示例：
```
feat: 新增供应商管理六件套与前端三件
fix: 修复供应商分页排序字段未转下划线导致SQL报错
refactor: 供应商查询改批量接口消除N+1
docs: 补充国密与安全指南
chore: 初始化 Snowy v3.0.0 原始代码基线
```

## 常用操作

```bash
git status                          # 提交前必看
git add <具体文件路径>               # 精确暂存
git commit -m "feat: xxx"
git log --oneline -10               # 查看最近提交
git diff --stat                     # 改动概览

# 大改动前打 tag 备份
git tag v0.1-重构前
```

## 禁止操作

- ❌ `git push --force` 到主分支
- ❌ `git reset --hard` 丢弃未确认的改动（先问用户）
- ❌ `git clean -fd`（会删未跟踪的新文件）
- ❌ 提交 node_modules / target / 日志 / 本地口令配置（含 Nacos 配置里的本机口令）

## 与项目管理命令的联动

`/sync` `/update-status` `/next` 依赖 git 历史分析提交记录；若命令执行中 git 不可用（`git rev-parse --is-inside-work-tree` 失败），自动降级为代码扫描 + 文档时间线模式。

## 检查清单

- [ ] git status 干净无关文件
- [ ] message 符合 type: 中文
- [ ] 未 push（除非用户明确要求）
- [ ] 提交的代码过了规范检查
