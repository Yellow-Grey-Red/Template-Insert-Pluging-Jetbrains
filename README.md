# Template - IntelliJ IDEA 插件

一个基于 IntelliJ IDEA 平台开发的插件，旨在帮助开发者快速管理和插入代码模板，减少重复编码工作，提高开发效率。

## 功能特性

- ✨ 可视化界面管理代码模板
- ⚡ 快速插入常用代码片段
- 📝 支持添加、编辑、删除模板
- 💾 本地 JSON 持久化存储
- ⌨️ 支持快捷键操作（Alt+Q）
- 🎨 JavaFX 现代化界面设计
- 🔄 自动处理特殊字符转义

## 使用场景

- 快速插入常用代码片段，提高开发效率
- 减少重复编码，降低出错率
- 方便管理和维护大量代码模板
- 调试第三方 APK 时快速插入日志代码

## 安装方法

### 方法一：从 IntelliJ IDEA 插件市场安装
1. 打开 IntelliJ IDEA
2. 进入 `File` → `Settings` → `Plugins`
3. 在搜索框中输入 "Template"
4. 点击 `Install` 按钮安装
5. 重启 IDE 生效

### 方法二：手动安装
1. 下载插件的 ZIP 文件
2. 打开 IntelliJ IDEA
3. 进入 `File` → `Settings` → `Plugins`
4. 点击右上角的齿轮图标，选择 `Install Plugin from Disk...`
5. 选择下载的 ZIP 文件
6. 重启 IDE 生效

## 使用说明

### 1. 打开模板管理窗口
- 点击工具栏上的插件图标
- 或使用快捷键 `Alt+Q`

### 2. 管理代码模板
- **添加模板**：点击 "添加" 按钮，填写模板标题和内容，点击 "保存"
- **编辑模板**：选中要编辑的模板，修改内容后点击 "保存"
- **删除模板**：选中要删除的模板，点击 "删除" 按钮
- **上下移动**：使用上下箭头调整模板顺序

### 3. 插入代码模板
- 在编辑器中定位到要插入模板的位置
- 使用快捷键 `Alt+Q` 打开模板列表
- 选择要插入的模板，点击 "插入" 按钮

## 技术栈

| 技术/框架 | 版本 | 用途 |
|---------|------|------|
| Java | 11 | 开发语言 |
| JavaFX | 11 | UI 框架 |
| Gradle | 7.4.2 | 构建工具 |
| IntelliJ IDEA Plugin SDK | 2021.2 | 插件开发框架 |
| Jackson | 2.13.0 | JSON 处理 |

## 项目结构

```
├── src/main/java/com/nfym/
│   ├── insert/           # 插入相关功能
│   ├── setting/          # 设置和数据管理
│   │   ├── Item.java     # 模板数据模型
│   │   ├── Mapper.java   # 数据持久化
│   │   ├── ShowWindow.java # 主窗口界面
│   │   └── VBoxFactory.java # UI 组件工厂
│   ├── InsertItem.java   # 插入功能入口
│   └── win.java          # 窗口显示入口
├── src/main/resources/META-INF/
│   ├── plugin.xml        # 插件配置
│   ├── *.fxml            # JavaFX 布局文件
│   └── *.png             # 图标资源
├── build.gradle.kts      # Gradle 配置
└── README.md             # 项目说明文档
```

## 支持的 IDE 版本

- IntelliJ IDEA 2021.2+
- 支持所有基于 IntelliJ 平台的 IDE（如 PyCharm、WebStorm 等）

## 开发与构建

### 开发环境
- JDK 11+
- IntelliJ IDEA 2021.2+
- Gradle 7.4.2+

### 构建命令

```bash
# 编译项目
./gradlew build

# 运行插件
./gradlew runIde

# 生成插件 ZIP 文件
./gradlew buildPlugin
```

## 更新日志

### v1.0.0 (2026-01-03)
- 初始版本发布
- 支持模板的添加、编辑、删除功能
- 支持快捷键插入模板
- 支持模板顺序调整
- 本地 JSON 持久化存储



## 作者

- GitHub: [Yellow-Grey-Red](https://github.com/Yellow-Grey-Red)


## 贡献

欢迎提交 Issue 和 Pull Request！

## 相关项目

- [Debug-APK](https://github.com/Yellow-Grey-Red/Debug-APK) - 用于调试第三方 APK 的工具

## 致谢

感谢所有使用和支持本插件的开发者！

---

**如果您觉得这个插件对您有帮助，请给我一个 Star ⭐！**

*界面设计可能不够完美，但功能实用，希望您能包容和支持！*

*如果你觉得界面很丑，审美有问题，请包容一些，因为我的大学老师也是这样认为的，我已经很努力了。*