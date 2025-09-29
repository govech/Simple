# Android Framework

一个现代化的Android应用开发框架，基于最新的Android开发技术栈构建，旨在提供高效、可维护、可扩展的项目模板。

## 🚀 技术栈

### 核心技术
- **UI层**: Jetpack Compose (声明式UI)
- **架构模式**: MVVM (Model-View-ViewModel)
- **异步处理**: Kotlin Flow + Coroutines
- **依赖注入**: Hilt
- **网络请求**: Retrofit + OkHttp
- **本地存储**: Room Database + DataStore
- **导航**: Navigation Compose

### 开发工具
- **构建系统**: Gradle Kotlin DSL
- **依赖管理**: Version Catalog
- **代码检查**: Ktlint + Detekt
- **图片加载**: Coil

## 📱 功能特性

### 🏗️ 架构设计
- **清晰的分层架构**: Data、Domain、Presentation三层架构
- **MVVM模式**: 响应式UI状态管理
- **Repository模式**: 统一的数据访问层
- **UseCase模式**: 封装业务逻辑

### 🌐 网络层
- **统一的请求/响应处理**: 基于Retrofit的网络请求封装
- **自动Token刷新**: 无感知的认证令牌管理
- **错误处理机制**: 统一的网络错误处理
- **网络状态监听**: 实时网络连接状态检测

### 💾 数据层
- **本地缓存策略**: Room数据库本地存储
- **用户偏好设置**: DataStore键值对存储
- **数据源切换**: 支持本地/远程数据源灵活切换

### 🎨 UI组件库
- **Material Design 3**: 现代化的UI设计语言
- **通用组件**: Loading、Error、Empty状态组件
- **深色模式**: 自适应主题切换
- **响应式设计**: 适配不同屏幕尺寸

### 🛠️ 工具类库
- **日志管理**: 分级日志输出系统
- **权限处理**: 简化的权限请求管理
- **网络监控**: 网络状态实时监控
- **时间处理**: 丰富的日期时间工具函数

## 📁 项目结构

```
app/
├── data/                           # 数据层
│   ├── local/                      # 本地数据源
│   │   ├── dao/                    # 数据访问对象
│   │   ├── database/               # 数据库配置
│   │   ├── datastore/              # DataStore配置
│   │   └── entity/                 # 数据库实体
│   ├── remote/                     # 远程数据源
│   │   ├── api/                    # API服务接口
│   │   ├── dto/                    # 数据传输对象
│   │   └── interceptor/            # 网络拦截器
│   ├── repository/                 # Repository实现
│   └── mapper/                     # 数据映射器
├── domain/                         # 领域层
│   ├── model/                      # 领域模型
│   ├── repository/                 # Repository接口
│   └── usecase/                    # 业务用例
├── presentation/                   # 表示层
│   ├── ui/                         # UI组件
│   │   ├── screens/                # 页面组件
│   │   ├── components/             # 通用UI组件
│   │   └── theme/                  # 主题配置
│   ├── viewmodel/                  # ViewModel
│   └── navigation/                 # 导航配置
├── di/                            # 依赖注入模块
├── utils/                         # 工具类
└── base/                          # 基础类
    ├── BaseViewModel              # ViewModel基类
    ├── BaseRepository             # Repository基类
    └── BaseUseCase               # UseCase基类
```

## 🏃‍♂️ 快速开始

### 环境要求
- Android Studio Flamingo | 2022.2.1 或更高版本
- JDK 8 或更高版本
- Android SDK API 24 (Android 7.0) 或更高版本
- Kotlin 1.9.20 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/android-framework.git
   cd android-framework
   ```

2. **打开项目**
   - 使用Android Studio打开项目目录
   - 等待Gradle同步完成

3. **配置环境**
   - 在`local.properties`中配置SDK路径
   - 根据需要修改`app/build.gradle.kts`中的配置

4. **运行项目**
   - 连接Android设备或启动模拟器
   - 点击运行按钮或执行`./gradlew assembleDebug`

## 🔧 配置说明

### 构建变体
项目支持三种构建变体：
- **debug**: 开发环境，启用日志和调试功能
- **staging**: 测试环境，模拟生产环境配置
- **release**: 生产环境，启用代码混淆和优化

### 环境配置
在`app/build.gradle.kts`中配置不同环境的API地址：
```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BASE_URL", "\"https://api-dev.example.com/\"")
    }
    create("staging") {
        buildConfigField("String", "BASE_URL", "\"https://api-staging.example.com/\"")
    }
    release {
        buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
    }
}
```

## 📚 使用指南

### 创建新页面
1. 在`presentation/ui/screens/`创建Screen组件
2. 在`presentation/viewmodel/`创建对应的ViewModel
3. 在`navigation/Screen.kt`中定义路由
4. 在`AndroidFrameworkNavigation.kt`中添加导航配置

### 添加网络请求
1. 在`data/remote/dto/`定义请求/响应DTO
2. 在`data/remote/api/`创建API服务接口
3. 在`domain/repository/`定义Repository接口
4. 在`data/repository/`实现Repository
5. 在`domain/usecase/`创建UseCase

### 添加数据库表
1. 在`data/local/entity/`创建Entity类
2. 在`data/local/dao/`创建DAO接口
3. 在`AppDatabase.kt`中注册Entity和DAO
4. 在`data/mapper/`创建数据映射器

## 🧪 测试

### 运行单元测试
```bash
./gradlew test
```

### 运行UI测试
```bash
./gradlew connectedAndroidTest
```

### 代码质量检查
```bash
./gradlew detekt
```

## 📋 开发规范

### 代码风格
- 遵循Kotlin官方代码规范
- 使用Ktlint进行代码格式化
- 使用Detekt进行静态代码分析

### 命名规范
- **类名**: 使用PascalCase，如`UserRepository`
- **函数名**: 使用camelCase，如`getUserInfo()`
- **变量名**: 使用camelCase，如`userName`
- **常量名**: 使用SCREAMING_SNAKE_CASE，如`MAX_RETRY_COUNT`

### Git提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建工具或辅助工具的变动
```

## 🤝 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 📄 许可证

本项目采用MIT许可证 - 查看[LICENSE](LICENSE)文件了解详情

## 📞 支持

如果您在使用过程中遇到问题，请通过以下方式获取帮助：
- 提交[Issue](https://github.com/your-username/android-framework/issues)
- 发送邮件至: support@example.com

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

**Android Framework** - 让Android开发更简单、更高效！
