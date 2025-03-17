# Ollama Java Client

[English](#english) | [中文](#中文)

<a name="english"></a>
# English

A Java client for the Ollama API, providing a simple and efficient way to interact with Ollama's language models.

## Features

- Text generation with streaming support
- Chat functionality
- Text embedding
- Model management (list, delete)
- Full support for Ollama API parameters
- Comprehensive error handling
- Detailed logging

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.ollama</groupId>
    <artifactId>ollama-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Quick Start

```java
import com.ollama.client.OllamaClient;
import com.ollama.client.model.*;

// Create client
OllamaClient client = new OllamaClient();

// Generate text
GenerateRequest request = new GenerateRequest();
request.setModel("qwen2.5:7b");  // Recommended model
request.setPrompt("Tell me a story");
GenerateResponse response = client.generate(request);
System.out.println(response.getResponse());

// Chat
ChatRequest chatRequest = new ChatRequest();
chatRequest.setModel("qwen2.5:7b");  // Recommended model
chatRequest.setMessages(Arrays.asList(
    new Message(Message.Role.USER, "Hello, how are you?")
));
ChatResponse chatResponse = client.chat(chatRequest);
System.out.println(chatResponse.getMessage().getContent());

// Stream generation
request.setStream(true);
client.generateStream(request)
    .forEach(r -> System.out.print(r.getResponse()));
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

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

<a name="中文"></a>
# 中文

Ollama API 的 Java 客户端，提供简单高效的方式与 Ollama 的语言模型进行交互。

## 特性

- 支持流式文本生成
- 聊天功能
- 文本嵌入
- 模型管理（列表、删除）
- 完整支持 Ollama API 参数
- 全面的错误处理
- 详细的日志记录

## 安装

在 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>com.ollama</groupId>
    <artifactId>ollama-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方法

### 快速开始

```java
import com.ollama.client.OllamaClient;
import com.ollama.client.model.*;

// 创建客户端
OllamaClient client = new OllamaClient();

// 生成文本
GenerateRequest request = new GenerateRequest();
request.setModel("qwen2.5:7b");  // 推荐模型
request.setPrompt("讲个故事");
GenerateResponse response = client.generate(request);
System.out.println(response.getResponse());

// 聊天
ChatRequest chatRequest = new ChatRequest();
chatRequest.setModel("qwen2.5:7b");  // 推荐模型
chatRequest.setMessages(Arrays.asList(
    new Message(Message.Role.USER, "你好，最近怎么样？")
));
ChatResponse chatResponse = client.chat(chatRequest);
System.out.println(chatResponse.getMessage().getContent());

// 流式生成
request.setStream(true);
client.generateStream(request)
    .forEach(r -> System.out.print(r.getResponse()));
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

## 贡献

贡献是受欢迎的！请随时提交 Pull Request。

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。 