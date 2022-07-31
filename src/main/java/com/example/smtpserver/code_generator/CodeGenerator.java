package com.example.smtpserver.code_generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

  public static void main(String[] args) {
    generate();
  }

  private static void generate() {
    FastAutoGenerator.create(
            "jdbc:mysql://localhost:3306/smtp_server?serverTimezone=GMT%2b8",
            "root", ".20010404liushun")
        .globalConfig(
            builder -> {
              builder
                  .author("ls") // 设置作者
                  .fileOverride() // 覆盖已生成文件
                  .outputDir("D:\\IdeaWorkspace\\smtpServer\\src\\main\\java\\"); // 指定输出目录
            })
        .packageConfig(
            builder -> {
              builder
                  .parent("com.example.smtpserver") // 设置父包名
                  .moduleName("") // 设置父包模块名
                  .pathInfo(
                      Collections.singletonMap(
                          OutputFile.mapperXml,
                          "D:\\IdeaWorkspace\\smtpServer\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("user"); // 设置需要生成的表名
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("email_box");
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("email");
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("filter_account");
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("log");
            })
        .strategyConfig(
            builder -> {
              builder.addInclude("server");
            })
        .execute();
  }
}
