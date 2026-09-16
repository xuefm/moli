# moli

后台管理系统的后端服务。基于 Spring Boot 4 + Java 21，提供账号、角色、资源（权限点）的 RBAC 管理，配套前端项目为 `moli-web`。

***

## 技术栈

| 组件                  | 版本 / 说明                                     |
| ------------------- | ------------------------------------------- |
| Spring Boot         | 4.1.1（Spring Framework 7）                   |
| JDK                 | 21                                          |
| MyBatis-Plus        | 3.5.15（`mybatis-plus-spring-boot4-starter`） |
| Spring Security     | 随 Boot 版本，JWT 无状态鉴权                         |
| JJWT                | 0.11.5                                      |
| MySQL               | 8.x（`mysql-connector-j`）                    |
| Redis               | 存登录态                                        |
| Hutool              | 5.8.9                                       |
| Knife4j             | 4.5.0（OpenAPI 3 文档 UI）                      |
| Lombok / Freemarker | 编译期 / MP 代码生成器依赖                            |

> 注意：本项目用的是 **Jackson 3**（Boot 4 默认），不是 Jackson 2，行为差异见文末「注意事项」。

***

## 环境要求

* JDK 21+

* Maven 3.6+

* MySQL 8（当前配置指向 `192.168.200.101:3306`）

* Redis（当前配置指向 `192.168.200.101:6379`）

***

## 快速开始

### 1. 建库并导入初始数据

```bash
mysql -u root -p -e "CREATE DATABASE moli DEFAULT CHARSET utf8mb4;"
mysql -u root -p moli < sql/moli.sql
```

`sql/moli.sql` 包含 5 张表的结构和一批初始数据（admin 账号、admin 角色、菜单与权限点）。

### 2. 修改配置

`src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://<你的MySQL地址>:3306/moli?...
    username: root
    password: <你的密码>
  data:
    redis:
      host: <你的Redis地址>
      port: 6379
      password: <你的Redis密码>
jwt:
  secret: <换成自己的随机串，至少 32 位>
```

### 3. 启动

```bash
mvn spring-boot:run
# 或
mvn clean package -DskipTests && java -jar target/moli-0.0.1-SNAPSHOT.jar
```

也可以直接用 IDE 启动 `io.github.xuefm.moli.MoliApplication`。

### 4. 验证

服务默认端口 **8080**，无 context-path。

| 地址                                  | 说明                |
| ----------------------------------- | ----------------- |
| <http://localhost:8080/doc.html>    | Knife4j 接口文档 UI   |
| <http://localhost:8080/v3/api-docs> | OpenAPI 3 原始 JSON |

***

## 初始账号

| 登录名     | 密码                                  |
| ------- | ----------------------------------- |
| `admin` | 见 `sql/moli.sql`（BCrypt 哈希存储，未记录明文） |

初始账号关联 `admin` 角色，拥有 `account:*`、`role:*`、`resource:*` 的查询/新增/修改权限。

**忘记密码时重置为** **`123456`**（直接用现成的 BCrypt 哈希）：

```sql
UPDATE `sys_account`
SET `login_password` = '2a2a2a10$nIiKPjVBwiC4QwrRrclJJOkAQaumniYjXKVmbEpukm4k6jbU0YYVC'
WHERE `login_name` = 'admin';
```

想设成别的密码，自己生成一个哈希：

```bash
# Windows（jar 版本按你本地 Maven 仓库实际情况调整）
echo 'System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("你的密码"));' \
  | jshell --class-path "D:/apache-maven-3.6.3/respository/org/springframework/security/spring-security-crypto/7.1.1/spring-security-crypto-7.1.1.jar;D:/apache-maven-3.6.3/respository/org/springframework/spring-jcl/6.2.8/spring-jcl-6.2.8.jar" -s -
```

> `spring-security-crypto` 依赖 `spring-jcl`（提供 commons-logging），少了会报 `NoClassDefFoundError: LogFactory`。

***

## 目录结构

```
src/main/java/io/github/xuefm/moli/
├─ MoliApplication.java            启动类
├─ config/
│  ├─ security/                    Security 配置与 JWT 过滤器
│  │  ├─ MySecurityConfig.java     过滤器链、CORS、BCrypt、permitAll 白名单
│  │  ├─ JWTAuthenticateFilter.java  解析 Authorization 头
│  │  ├─ AuthenticationEntryPointImpl.java  未认证处理
│  │  └─ AccessDeniedHandlerImpl.java       拒绝访问处理
│  ├─ mybatis/                     MP 插件（分页、乐观锁）与自动填充
│  ├─ RedisConfiguration.java
│  ├─ MinIoConfig.java             （当前未启用，@Configuration 已注释）
│  └─ InitSystemBeanConfig.java
├─ controller/                     LoginController + 账号 / 角色 / 资源
├─ service/                        业务接口与实现（含 UserDetailsServiceImpl）
├─ mapper/                         MyBatis Mapper 接口
├─ entity/                         数据库实体（SysAccount / SysRole / SysResource / 两张关联表）
├─ data/                           VO、请求体、统一响应体、缓存常量
├─ aop/                            日志、防重复提交
├─ handler/                        GlobalExceptionHandler 全局异常处理
├─ expection/                      自定义异常
└─ util/                           JwtUtil、IPUtils

src/main/resources/
├─ application.yml
└─ mapper/*.xml                    手写 SQL
```

***

## 数据库

| 表                   | 说明                            |
| ------------------- | ----------------------------- |
| `sys_account`       | 账号                            |
| `sys_role`          | 角色                            |
| `sys_resource`      | 资源 / 权限点（`type`：0 组（菜单）、1 接口） |
| `sys_account_role`  | 账号 ↔ 角色（多对多）                  |
| `sys_role_resource` | 角色 ↔ 资源（多对多）                  |

* 5 张表都有 `deleted` 字段，全局开启**逻辑删除**（`mybatis-plus.global-config.db-config.logic-delete-field: deleted`），所有 `deleteById` / 查询都会自动带上条件，**不会真删数据**。

* 资源树靠 `superior_id` + `level` 组织：**组（type=0）可挂下级，接口（type=1）是叶子**。

***

## 接口一览

### 登录模块（无需鉴权）

| 方法   | 路径             | 说明             |
| ---- | -------------- | -------------- |
| POST | `/user/login`  | 登录，返回 JWT      |
| POST | `/user/logout` | 登出，清 Redis 登录态 |

### 账号

| 方法     | 路径                             | 权限码              |
| ------ | ------------------------------ | ---------------- |
| GET    | `/sys/sysAccount/list`         | `account:select` |
| GET    | `/sys/sysAccount/details/{id}` | `account:select` |
| POST   | `/sys/sysAccount`              | `account:insert` |
| PUT    | `/sys/sysAccount`              | `account:update` |
| DELETE | `/sys/sysAccount/{id}`         | `account:delete` |

### 角色

| 方法     | 路径                  | 权限码           |
| ------ | ------------------- | ------------- |
| GET    | `/sys/sysRole/list` | `role:select` |
| GET    | `/sys/sysRole/all`  | `role:select` |
| POST   | `/sys/sysRole`      | `role:insert` |
| PUT    | `/sys/sysRole`      | `role:update` |
| DELETE | `/sys/sysRole/{id}` | `role:delete` |

### 资源

| 方法     | 路径                               | 权限码               |
| ------ | -------------------------------- | ----------------- |
| GET    | `/sys/sysResource/list`          | `resource:select` |
| GET    | `/sys/sysResource/treeAll`       | `resource:select` |
| GET    | `/sys/sysResource/getStepByStep` | `resource:select` |
| GET    | `/sys/sysResource/byRoleId/{id}` | `resource:select` |
| POST   | `/sys/sysResource`               | `resource:insert` |
| PUT    | `/sys/sysResource`               | `resource:update` |
| DELETE | `/sys/sysResource/{id}`          | `resource:delete` |

三个 delete 权限点（`account:delete` / `role:delete` / `resource:delete`）是后加的，老库需要执行 `sql/add-delete-permission.sql` 才有，否则返回 403。

***

## 鉴权与权限模型

### 登录流程

1. `POST /user/login` 提交 `loginName` + `loginPassword`
2. `UserDetailsServiceImpl.loadUserByUsername` 查账号，并从 `sys_resource` 联查出该账号拥有的**权限 code 列表**
3. BCrypt 校验密码，通过后签发 JWT，登录态写入 Redis
4. 后续请求在请求头带 JWT

### 请求头约定

```
Authorization: eyJhbGciOiJIUzI1NiJ9...
```

**值是裸 token，不要加** **`Bearer `** **前缀**——`JwtUtil.verification` 直接按 JWT 解析。

### 权限判断

* Controller 方法上用 `@PreAuthorize("hasAuthority('xxx:yyy')")`

* 权限码来自 `sys_resource.code`，形如 `account:select`、`role:delete`

* 关系是：账号 → 角色 → 资源，一个账号可以有多个角色，权限取并集

* 白名单（无需登录）：`/user/login`、`/public/**`、`/doc.html`、`/swagger-ui.html`、`/swagger-resources/**`、`/webjars/**`、`/v3/api-docs/**`、`/api/**`

* Session 策略 `STATELESS`，CSRF 关闭，CORS 全开

***

## 统一响应

所有接口返回 `Results<T>`：

```json
{ "status": 200, "message": "请求成功", "data": {} }
```

| status | 含义                               |
| ------ | -------------------------------- |
| `200`  | 成功                               |
| `9999` | 业务失败（`BusinessException`）        |
| `403`  | 已登录但没权限（`AccessDeniedException`） |

`message` 为 `null` 时不序列化。参数校验失败时 `data` 是 `{ 字段名: 错误原因 }`。

分页统一用 `PageData<T>`（`current` / `size` / `total` / `list` / `totalPages`）。

***

## 已内置的机制

* **逻辑删除**：全局配置，5 张表统一用 `deleted` 字段

* **自动填充**：`MyMetaObjectHandler` 自动写 `createTime` / `updateTime` / `createById` / `updateById`（当前创建人硬编码为 `root`）

* **分页 + 乐观锁**：`MybatisPlusConfig` 注册了 `PaginationInnerInterceptor` 和 `OptimisticLockerInnerInterceptor`

* **全局异常处理**：`GlobalExceptionHandler` 捕获业务异常、参数校验异常、`AccessDeniedException` 等

* **AOP**：`WebLogAspect` 请求日志、`RepeatSubmitAspect` + `@RepeatSubmit` 防重复提交

* **JWT 过滤**：`JWTAuthenticateFilter` 在 `UsernamePasswordAuthenticationFilter` 之前执行

***

## 注意事项

### 1. Jackson 3 会把响应字段按 a-z 排序

Boot 4 用的是 Jackson 3，它把 `SORT_PROPERTIES_ALPHABETICALLY` 的默认值改成了 `true`（Jackson 2 是 `false`）。所以如果不配置，返回对象字段会变成 `code / createTime / enabled / id / title` 这种字母序，而不是实体类的声明顺序。

`application.yml` 里已经关掉了：

```yaml
spring:
  jackson:
    mapper:
      sort-properties-alphabetically: false
```

如果某个类的顺序必须固定（不依赖 JVM 反射顺序），在类上加 `@JsonPropertyOrder({...})`，它的优先级高于排序特性——`Results` 就是这么做的。

### 2. 401 / 403 返回的 HTTP 状态码还是 200

* `AuthenticationEntryPointImpl` 用 `HttpStatus.OK`，返回 HTTP 200 + `{"status":9999,"message":"认证失败"}`

* `GlobalExceptionHandler.accessDeniedExceptionHandler` 没加 `@ResponseStatus`，返回 HTTP 200 + `{"status":403,"message":"拒绝访问"}`

HTTP 语义不正确，前端只能靠 body 里的 `status` 或 `message` 文案判断。建议改成：

```java
@ResponseStatus(HttpStatus.UNAUTHORIZED)      // 401
@ResponseStatus(HttpStatus.FORBIDDEN)         // 403
```

前端 `moli-web` 已按 `status` 字段兼容：403 只提示不退出登录，401 才跳登录页。

### 3. 删除资源不会级联删除下级

`DELETE /sys/sysResource/{id}` 只删该资源 + 它与角色的关联。如果它有下级，下级的 `superior_id` 会变成悬空引用，资源树里就显示不出来了。删之前请确认没有下级。

### 4. 生产环境前要改的东西

* `jwt.secret` 当前硬编码在 `application.yml`，必须换成随机值并走环境变量

* 数据库 / Redis 密码明文写在配置文件里

* CORS 现在是 `allowedOrigins("*")` 全开

* `spring-boot-starter-aop` 显式指定了 `4.0.0-M2`（里程碑版），与 Boot 4.1.1 混用，建议去掉版本号让 Boot 统一管理

* MyBatis-Plus 开启了 `log-impl: StdOutImpl`，会打印全部 SQL，生产建议关掉

### 5. 未启用的部分

* `MinIoConfig` 的 `@Configuration` 已注释，MinIO 依赖在但未生效，`application.yml` 里也没有 minio 配置段；`data/file/UploadVO`、`GetUrlRequest` 是预留的文件上传模型，暂无对应接口。

* `aop/annotation/RepeatSubmit` 防重注解已定义，需要自己在方法上使用。

***

## 配套前端

[moli-web](../moli-web) —— Vue 3 + Vite + Element Plus，覆盖登录、账号、角色、资源管理。

前后端约定见 `moli-web/README.md`（重点：请求头 `Authorization`、403 不退出登录、权限树只提交叶子节点）。
