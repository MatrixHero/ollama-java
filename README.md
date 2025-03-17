# Ollama Java Client

[![Java](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/yourusername/ollama-java)](https://github.com/yourusername/ollama-java/releases)

[English](#english) | [中文](#中文)

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Spring Boot Integration](#spring-boot-integration)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

<a name="english"></a>
# English

A Java client for the Ollama API, providing a simple and efficient way to interact with Ollama's language models.

## Features

- **Text Generation**
  - Support for streaming responses
  - Customizable generation parameters
  - System prompt and template support
- **Chat Functionality**
  - Multi-turn conversation support
  - Role-based message handling
  - Streaming chat responses
- **Text Embedding**
  - Vector embeddings for text
  - Support for multiple models
- **Model Management**
  - List available models
  - Delete models
  - Model information retrieval
- **Advanced Features**
  - Full support for Ollama API parameters
  - Comprehensive error handling
  - Detailed logging with SLF4J
  - Spring Boot integration
  - Configurable HTTP client
  - Customizable timeouts

## Installation

Download the latest release from the [releases page](https://github.com/yourusername/ollama-java/releases) and add it to your project's dependencies.

## Prerequisites

- Java 8 or higher
- Ollama server running locally or accessible via network

### Installing Ollama

1. **macOS**
   ```bash
   curl -fsSL https://ollama.com/install.sh | sh
   ```

2. **Linux**
   ```bash
   curl -fsSL https://ollama.com/install.sh | sh
   ```

3. **Windows**
   - Download the installer from [Ollama's official website](https://ollama.com/download)
   - Run the installer and follow the setup wizard

After installation, start the Ollama service:
```bash
ollama serve
```

In a new terminal, you can pull the recommended model:
```bash
ollama pull qwen2.5:7b
```

## Usage

### Quick Start

```java
import com.matrixhero.ollama.client.OllamaClient;
import com.matrixhero.ollama.client.model.*;

// Create client
OllamaClient client = new OllamaClient();

// Generate text
GenerateRequest request = new GenerateRequest();
request.

setModel("qwen2.5:7b");  // Recommended model
request.

setPrompt("Tell me a story");

GenerateResponse response = client.generate(request);
System.out.

println(response.getResponse());

// Chat
ChatRequest chatRequest = new ChatRequest();
chatRequest.

setModel("qwen2.5:7b");  // Recommended model
chatRequest.

setMessages(Arrays.asList(
        new Message(Message.Role.USER, "Hello, how are you?")
));
ChatResponse chatResponse = client.chat(chatRequest);
System.out.

println(chatResponse.getMessage().

getContent());

// Stream generation
        request.

setStream(true);
client.

generateStream(request)
    .

forEach(r ->System.out.

print(r.getResponse()));
```

### Recommended Model

We recommend using the Qwen model (qwen2.5:7b) for best performance and quality. Qwen is a powerful language model developed by Alibaba Cloud, offering excellent performance in both English and Chinese tasks.

For more information about Qwen, please visit:
- GitHub: [QwenLM/Qwen](https://github.com/QwenLM/Qwen)
- Model Card: [Qwen/Qwen2.5-7B](https://huggingface.co/Qwen/Qwen2.5-7B)

### API Documentation

#### Generate Text

```java
GenerateRequest request = new GenerateRequest();
request.setModel("qwen2.5:7b");
request.setPrompt("Your prompt here");
request.setSystem("Optional system prompt");
request.setTemplate("Optional template");
request.setContext(new long[]{...});  // Optional context
request.setStream(true);  // Enable streaming
request.setOptions(new Options());  // Optional generation options

GenerateResponse response = client.generate(request);
```

#### Chat

```java
ChatRequest request = new ChatRequest();
request.setModel("qwen2.5:7b");
request.setMessages(Arrays.asList(
    new Message(Message.Role.USER, "User message"),
    new Message(Message.Role.ASSISTANT, "Assistant message")
));
request.setStream(true);  // Enable streaming
request.setOptions(new Options());  // Optional generation options

ChatResponse response = client.chat(request);
```

#### Embed Text

```java
EmbedRequest request = new EmbedRequest();
request.setModel("qwen2.5:7b");
request.setInput("Text to embed");

EmbedResponse response = client.embed(request);
float[] embedding = response.getEmbedding();
```

#### List Models

```java
ListResponse response = client.list();
List<Model> models = response.getModels();
```

#### Delete Model

```java
DeleteRequest request = new DeleteRequest();
request.setName("model-name");
client.delete(request);
```

### Error Handling

The client includes comprehensive error handling:

```java
try {
    GenerateResponse response = client.generate(request);
} catch (IOException e) {
    // Handle network errors
    log.error("Error generating text", e);
} catch (Exception e) {
    // Handle other errors
    log.error("Unexpected error", e);
}
```

### Logging

The client uses SLF4J for logging. Configure your logging framework to see detailed logs:

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.11</version>
</dependency>
```

## Spring Boot Integration

If you're using Spring Boot, you can easily integrate the Ollama client:

```java
@Configuration
public class OllamaConfig {
    @Bean
    public OllamaClient ollamaClient() {
        return new OllamaClient();
    }
}
```

## Development

### Building from Source

```bash
git clone https://github.com/yourusername/ollama-java.git
cd ollama-java
./gradlew build
```

### Running Tests

```bash
./gradlew test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. Before submitting, please:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## FAQ

### Q: What is the default Ollama server URL?
A: The client connects to `http://localhost:11434` by default. You can configure a different URL using the `OllamaClient` constructor.

### Q: How do I handle rate limiting?
A: The client includes built-in retry mechanisms for rate limiting. You can configure retry behavior through the client options.

### Q: Can I use this client with other Ollama models?
A: Yes, the client supports all Ollama models. We recommend Qwen for best performance, but you can use any model available in your Ollama installation.

## Examples

Check out our [examples directory](examples) for more detailed usage examples:
- [Basic text generation](examples/src/main/java/com/ollama/examples/GenerateExample.java)
- [Chat with streaming](examples/src/main/java/com/ollama/examples/ChatStreamExample.java)
- [Spring Boot integration](examples/src/main/java/com/ollama/examples/spring/SpringBootExample.java)

### Client Configuration

#### Timeout Settings

You can configure various timeout settings for the client using the builder pattern:

```java
// Create a client with custom timeouts
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);

// Or set timeouts individually
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);
```

Default timeout values:
- Connection timeout: 10 seconds
- Read timeout: 30 seconds
- Write timeout: 10 seconds

When a timeout occurs, the client will throw an `OllamaTimeoutException` with a descriptive message indicating which operation timed out. You can catch this exception to handle timeout scenarios:

```java
try {
    ChatResponse response = client.chat(request);
} catch (OllamaTimeoutException e) {
    // Handle timeout
    System.err.println("Request timed out: " + e.getMessage());
} catch (IOException e) {
    // Handle other IO errors
    System.err.println("IO error: " + e.getMessage());
}
```

### 客户端配置

#### 超时设置

你可以使用构建器模式配置客户端的各种超时设置：

```java
// 创建具有自定义超时的客户端
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);

// 或者单独设置超时
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);
```

默认超时值：
- 连接超时：10秒
- 读取超时：30秒
- 写入超时：10秒

当发生超时时，客户端将抛出带有描述性消息的 `OllamaTimeoutException`，指示哪个操作超时。你可以捕获此异常来处理超时情况：

```java
try {
    ChatResponse response = client.chat(request);
} catch (OllamaTimeoutException e) {
    // 处理超时
    System.err.println("请求超时: " + e.getMessage());
} catch (IOException e) {
    // 处理其他IO错误
    System.err.println("IO错误: " + e.getMessage());
}
```

---

<a name="中文"></a>
# 中文

Ollama API 的 Java 客户端，提供简单高效的方式与 Ollama 的语言模型进行交互。

## 特性

- **文本生成**
  - 支持流式响应
  - 可定制生成参数
  - 系统提示词和模板支持
- **聊天功能**
  - 多轮对话支持
  - 基于角色的消息处理
  - 流式聊天响应
- **文本嵌入**
  - 文本向量嵌入
  - 支持多种模型
- **模型管理**
  - 列出可用模型
  - 删除模型
  - 模型信息检索
- **高级功能**
  - 完整支持 Ollama API 参数
  - 全面的错误处理
  - 详细的日志记录
  - Spring Boot 集成
  - 可配置的 HTTP 客户端
  - 可自定义的超时设置

## 安装

从 [releases 页面](https://github.com/yourusername/ollama-java/releases) 下载最新版本并添加到你的项目依赖中。

## 前置条件

- Java 8 或更高版本
- 本地运行或通过网络可访问的 Ollama 服务器

### 安装 Ollama

1. **macOS**
   ```bash
   curl -fsSL https://ollama.com/install.sh | sh
   ```

2. **Linux**
   ```bash
   curl -fsSL https://ollama.com/install.sh | sh
   ```

3. **Windows**
   - 从 [Ollama 官网](https://ollama.com/download) 下载安装程序
   - 运行安装程序并按照安装向导操作

安装完成后，启动 Ollama 服务：
```bash
ollama serve
```

在新的终端中，你可以拉取推荐的模型：
```bash
ollama pull qwen2.5:7b
```

## 使用方法

### 快速开始

```java
import com.matrixhero.ollama.client.OllamaClient;
import com.matrixhero.ollama.client.model.*;

// 创建客户端
OllamaClient client = new OllamaClient();

// 生成文本
GenerateRequest request = new GenerateRequest();
request.

setModel("qwen2.5:7b");  // 推荐模型
request.

setPrompt("讲个故事");

GenerateResponse response = client.generate(request);
System.out.

println(response.getResponse());

// 聊天
ChatRequest chatRequest = new ChatRequest();
chatRequest.

setModel("qwen2.5:7b");  // 推荐模型
chatRequest.

setMessages(Arrays.asList(
        new Message(Message.Role.USER, "你好，最近怎么样？")
));
ChatResponse chatResponse = client.chat(chatRequest);
System.out.

println(chatResponse.getMessage().

getContent());

// 流式生成
        request.

setStream(true);
client.

generateStream(request)
    .

forEach(r ->System.out.

print(r.getResponse()));
```

### 推荐模型

我们推荐使用 Qwen 模型（qwen2.5:7b）以获得最佳性能和质量。Qwen 是由阿里云开发的强大语言模型，在英文和中文任务中都表现出色。

关于 Qwen 的更多信息，请访问：
- GitHub: [QwenLM/Qwen](https://github.com/QwenLM/Qwen)
- 模型卡片: [Qwen/Qwen2.5-7B](https://huggingface.co/Qwen/Qwen2.5-7B)

### API 文档

#### 生成文本

```java
GenerateRequest request = new GenerateRequest();
request.setModel("qwen2.5:7b");
request.setPrompt("你的提示词");
request.setSystem("可选的系统提示词");
request.setTemplate("可选的模板");
request.setContext(new long[]{...});  // 可选的上下文
request.setStream(true);  // 启用流式输出
request.setOptions(new Options());  // 可选的生成选项

GenerateResponse response = client.generate(request);
```

#### 聊天

```java
ChatRequest request = new ChatRequest();
request.setModel("qwen2.5:7b");
request.setMessages(Arrays.asList(
    new Message(Message.Role.USER, "用户消息"),
    new Message(Message.Role.ASSISTANT, "助手消息")
));
request.setStream(true);  // 启用流式输出
request.setOptions(new Options());  // 可选的生成选项

ChatResponse response = client.chat(request);
```

#### 文本嵌入

```java
EmbedRequest request = new EmbedRequest();
request.setModel("qwen2.5:7b");
request.setInput("要嵌入的文本");

EmbedResponse response = client.embed(request);
float[] embedding = response.getEmbedding();
```

#### 列出模型

```java
ListResponse response = client.list();
List<Model> models = response.getModels();
```

#### 删除模型

```java
DeleteRequest request = new DeleteRequest();
request.setName("模型名称");
client.delete(request);
```

### 错误处理

客户端包含全面的错误处理：

```java
try {
    GenerateResponse response = client.generate(request);
} catch (IOException e) {
    // 处理网络错误
    log.error("生成文本时出错", e);
} catch (Exception e) {
    // 处理其他错误
    log.error("意外错误", e);
}
```

### 日志记录

客户端使用 SLF4J 进行日志记录。配置你的日志框架以查看详细日志：

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.11</version>
</dependency>
```

## Spring Boot 集成

如果你使用 Spring Boot，可以轻松集成 Ollama 客户端：

```java
@Configuration
public class OllamaConfig {
    @Bean
    public OllamaClient ollamaClient() {
        return new OllamaClient();
    }
}
```

## 开发

### 从源码构建

```bash
git clone https://github.com/yourusername/ollama-java.git
cd ollama-java
./gradlew build
```

### 运行测试

```bash
./gradlew test
```

## 贡献

贡献是受欢迎的！请随时提交 Pull Request。

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 常见问题

### Q: Ollama 服务器的默认 URL 是什么？
A: The client connects to `http://localhost:11434` by default. You can configure a different URL using the `OllamaClient` constructor.

### Q: 如何处理速率限制？
A: The client includes built-in retry mechanisms for rate limiting. You can configure retry behavior through the client options.

### Q: 我可以使用其他 Ollama 模型吗？
A: Yes, the client supports all Ollama models. We recommend Qwen for best performance, but you can use any model available in your Ollama installation.

## 示例

查看我们的 [examples 目录](examples) 获取更详细的使用示例：
- [基础文本生成](examples/src/main/java/com/ollama/examples/GenerateExample.java)
- [流式聊天](examples/src/main/java/com/ollama/examples/ChatStreamExample.java)
- [Spring Boot 集成](examples/src/main/java/com/ollama/examples/spring/SpringBootExample.java)

### 客户端配置

#### 超时设置

你可以使用构建器模式配置客户端的各种超时设置：

```java
// Create a client with custom timeouts
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);

// Or set timeouts individually
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);
```

Default timeout values:
- Connection timeout: 10 seconds
- Read timeout: 30 seconds
- Write timeout: 10 seconds

When a timeout occurs, the client will throw an `OllamaTimeoutException` with a descriptive message indicating which operation timed out. You can catch this exception to handle timeout scenarios:

```java
try {
    ChatResponse response = client.chat(request);
} catch (OllamaTimeoutException e) {
    // Handle timeout
    System.err.println("Request timed out: " + e.getMessage());
} catch (IOException e) {
    // Handle other IO errors
    System.err.println("IO error: " + e.getMessage());
}
```

### 客户端配置

#### 超时设置

你可以使用构建器模式配置客户端的各种超时设置：

```java
// 创建具有自定义超时的客户端
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);

// 或者单独设置超时
OllamaClient client = new OllamaClient()
    .withConnectTimeout(60)
    .withReadTimeout(120)
    .withWriteTimeout(60);
```

默认超时值：
- 连接超时：10秒
- 读取超时：30秒
- 写入超时：10秒

当发生超时时，客户端将抛出带有描述性消息的 `OllamaTimeoutException`，指示哪个操作超时。你可以捕获此异常来处理超时情况：

```java
try {
    ChatResponse response = client.chat(request);
} catch (OllamaTimeoutException e) {
    // 处理超时
    System.err.println("请求超时: " + e.getMessage());
} catch (IOException e) {
    // 处理其他IO错误
    System.err.println("IO错误: " + e.getMessage());
}
```

--- 