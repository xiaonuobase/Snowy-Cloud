/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos;

import com.alibaba.nacos.sys.filter.NacosTypeExcludeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Nacos starter.
 * 开发环境调试使用
 *
 * @author nacos
 */
@ComponentScan(basePackages = "com.alibaba.nacos", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {NacosTypeExcludeFilter.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {TypeExcludeFilter.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {AutoConfigurationExcludeFilter.class})})
@ServletComponentScan
@EnableScheduling
public class SnowyNacosApp {
    
    public static void main(String[] args) {

        // 注意：工程目录不要使用中文
        // 环境变量设置 单机模式 启动
        System.setProperty("nacos.standalone", "true");
        System.setProperty("nacos.core.auth.enabled", "true");
        System.setProperty("server.tomcat.basedir", "logs/snowy-nacos-app-log");

        SpringApplication.run(SnowyNacosApp.class, args);
    }
}

