package com.matrixhero.ollama.client.agent;

import com.matrixhero.ollama.client.OllamaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherAgentTest {
    private OllamaClient ollamaClient;
    private WeatherAgent agent;

    @BeforeEach
    void setUp() {
        ollamaClient = new OllamaClient();
        agent = new WeatherAgent(ollamaClient);
    }

    @Test
    void testWeatherQuery() throws Exception {
        // 使用你的 OpenWeatherMap API key
        // 创建新的 agent 实例用于实际 API 测试
        WeatherAgent realAgent = new WeatherAgent(ollamaClient);

        // 测试中文城市名
        String response = realAgent.execute("北京和纽约的天气如何？");
        System.out.println("北京天气查询结果：\n" + response);
//        assertTrue(response.contains("北京"));
//        assertTrue(response.contains("温度"));
//        assertTrue(response.contains("湿度"));

        // 测试英文城市名
        response = realAgent.execute("What's the weather in New York?");
        System.out.println("New York天气查询结果：\n" + response);
//        assertTrue(response.contains("New York"));
//        assertTrue(response.contains("temperature"));
//        assertTrue(response.contains("humidity"));

        // 测试无效城市名
        response = realAgent.execute("不存在的城市天气如何？");
        System.out.println("无效城市查询结果：\n" + response);
//        assertTrue(response.contains("Sorry"));
    }

    @Test
    void testAgentCapabilities() {
        // 测试名称
        assertEquals("weather", agent.getName());

        // 测试描述
        assertTrue(agent.getDescription().contains("天气"));

        // 测试输入识别
        assertTrue(agent.canHandle("北京天气如何？"));
        assertTrue(agent.canHandle("今天天气怎么样？"));
        assertTrue(agent.canHandle("What's the weather in London?"));
        assertFalse(agent.canHandle("讲个故事"));
        assertFalse(agent.canHandle("帮我写代码"));
    }

    @Test
    void testCityExtractionWithLLM() throws Exception {
        // 测试中文城市名
        String response = agent.execute("我想知道北京今天的天气情况");
        assertTrue(response.contains("Beijing"));

        response = agent.execute("上海明天会下雨吗？");
        assertTrue(response.contains("Shanghai"));

        response = agent.execute("广州最近一周的天气怎么样");
        assertTrue(response.contains("Guangzhou"));

        // 测试英文城市名
        response = agent.execute("What's the weather like in London today?");
        assertTrue(response.contains("London"));

        response = agent.execute("Temperature in New York tomorrow");
        assertTrue(response.contains("New York"));

        response = agent.execute("How's the weather in Paris this weekend?");
        assertTrue(response.contains("Paris"));

        // 测试中英文混合输入
        response = agent.execute("我想去深圳玩，那边天气怎么样？");
        assertTrue(response.contains("Shenzhen"));

        response = agent.execute("I'm planning to visit Tokyo next month, what's the weather like?");
        assertTrue(response.contains("Tokyo"));

        // 测试复杂中文输入
        response = agent.execute("杭州西湖的天气如何？");
        assertTrue(response.contains("Hangzhou"));

        response = agent.execute("成都的天气怎么样？");
        assertTrue(response.contains("Chengdu"));

        // 测试复杂英文输入
        response = agent.execute("What's the weather like in San Francisco Bay Area?");
        assertTrue(response.contains("San Francisco"));

        response = agent.execute("Temperature in New York City tomorrow");
        assertTrue(response.contains("New York"));

        // 测试无效输入
        response = agent.execute("今天天气真不错");
        assertTrue(response.contains("抱歉"));

        response = agent.execute("The weather is beautiful today");
        assertTrue(response.contains("抱歉"));
    }

    @Test
    void testCustomOllamaClient() throws Exception {
        // 创建一个自定义的 OllamaClient，可以设置不同的超时时间
        OllamaClient customClient = new OllamaClient()
            .withConnectTimeout(30)
            .withReadTimeout(60)
            .withWriteTimeout(30);

        WeatherAgent customAgent = new WeatherAgent(customClient);

        // 测试使用自定义客户端
        String response = customAgent.execute("北京天气如何？");
        assertTrue(response.contains("北京"));
        assertTrue(response.contains("温度"));
        assertTrue(response.contains("湿度"));
    }

    @Test
    void testUseAgentsParameter() throws Exception {
        // 创建一个新的 agent 实例用于实际 API 测试
        WeatherAgent realAgent = new WeatherAgent(ollamaClient);

        // 测试禁用 agents 的情况
        String response = realAgent.execute("北京天气如何？");
        assertTrue(response.contains("抱歉")); // 应该返回默认的无法识别消息

        // 测试启用 agents 的情况
        response = realAgent.execute("北京天气如何？");
        assertTrue(response.contains("北京"));
        assertTrue(response.contains("温度"));
        assertTrue(response.contains("湿度"));
    }
} 