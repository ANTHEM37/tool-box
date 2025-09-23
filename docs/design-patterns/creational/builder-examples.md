# 建造者模式 - 实际应用示例

## 🧪 实际应用示例

### 1. 计算机建造者

```java
/**
 * 计算机产品类
 */
public class Computer {
    private String cpu;
    private String memory;
    private String storage;
    private String graphics;

    // Setters
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public void showConfiguration() {
        System.out.println("=== 计算机配置 ===");
        System.out.println("CPU: " + cpu);
        System.out.println("内存: " + memory);
        System.out.println("存储: " + storage);
        System.out.println("显卡: " + graphics);
    }
}

/**
 * 计算机建造者接口
 */
public interface ComputerBuilder {
    ComputerBuilder buildCpu();

    ComputerBuilder buildMemory();

    ComputerBuilder buildStorage();

    ComputerBuilder buildGraphics();

    Computer build();
}

/**
 * 游戏电脑建造者
 */
public class GamingComputerBuilder implements ComputerBuilder {
    private Computer computer = new Computer();

    @Override
    public ComputerBuilder buildCpu() {
        computer.setCpu("Intel Core i9-13900K");
        return this;
    }

    @Override
    public ComputerBuilder buildMemory() {
        computer.setMemory("32GB DDR5-5600");
        return this;
    }

    @Override
    public ComputerBuilder buildStorage() {
        computer.setStorage("2TB NVMe SSD");
        return this;
    }

    @Override
    public ComputerBuilder buildGraphics() {
        computer.setGraphics("NVIDIA RTX 4080");
        return this;
    }

    @Override
    public Computer build() {
        return computer;
    }
}

/**
 * 办公电脑建造者
 */
public class OfficeComputerBuilder implements ComputerBuilder {
    private Computer computer = new Computer();

    @Override
    public ComputerBuilder buildCpu() {
        computer.setCpu("Intel Core i5-13400");
        return this;
    }

    @Override
    public ComputerBuilder buildMemory() {
        computer.setMemory("16GB DDR4-3200");
        return this;
    }

    @Override
    public ComputerBuilder buildStorage() {
        computer.setStorage("512GB SATA SSD");
        return this;
    }

    @Override
    public ComputerBuilder buildGraphics() {
        computer.setGraphics("集成显卡");
        return this;
    }

    @Override
    public Computer build() {
        return computer;
    }
}

// 使用示例
public class ComputerBuilderDemo {
    public static void main(String[] args) {
        // 构建游戏电脑
        Computer gamingComputer = new GamingComputerBuilder()
                .buildCpu()
                .buildMemory()
                .buildStorage()
                .buildGraphics()
                .build();

        gamingComputer.showConfiguration();

        // 构建办公电脑
        Computer officeComputer = new OfficeComputerBuilder()
                .buildCpu()
                .buildMemory()
                .buildStorage()
                .buildGraphics()
                .build();

        officeComputer.showConfiguration();
    }
}
```

### 2. SQL查询建造者

```java
/**
 * SQL查询类
 */
public class SqlQuery {
    private StringBuilder query = new StringBuilder();

    public void append(String sql) {
        query.append(sql);
    }

    public String getQuery() {
        return query.toString();
    }

    @Override
    public String toString() {
        return query.toString();
    }
}

/**
 * SQL建造者
 */
public class SqlQueryBuilder {
    private SqlQuery query = new SqlQuery();
    private boolean hasWhere = false;

    public SqlQueryBuilder select(String... columns) {
        query.append("SELECT ");
        if (columns.length == 0) {
            query.append("*");
        } else {
            query.append(String.join(", ", columns));
        }
        return this;
    }

    public SqlQueryBuilder from(String table) {
        query.append(" FROM " + table);
        return this;
    }

    public SqlQueryBuilder where(String condition) {
        query.append(" WHERE " + condition);
        hasWhere = true;
        return this;
    }

    public SqlQueryBuilder and(String condition) {
        if (hasWhere) {
            query.append(" AND " + condition);
        } else {
            where(condition);
        }
        return this;
    }

    public SqlQueryBuilder orderBy(String column) {
        query.append(" ORDER BY " + column);
        return this;
    }

    public SqlQueryBuilder limit(int count) {
        query.append(" LIMIT " + count);
        return this;
    }

    public SqlQuery build() {
        return query;
    }
}

// 使用示例
public class SqlBuilderDemo {
    public static void main(String[] args) {
        SqlQuery query = new SqlQueryBuilder()
                .select("id", "name", "email")
                .from("users")
                .where("age > 18")
                .and("status = 'active'")
                .orderBy("name")
                .limit(10)
                .build();

        System.out.println("生成的SQL: " + query);
    }
}
```

### 3. HTTP请求建造者

```java
/**
 * HTTP请求类
 */
public class HttpRequest {
    private String method;
    private String url;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();
    private String body;

    // Getters and Setters
    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void execute() {
        System.out.println("执行HTTP请求:");
        System.out.println("方法: " + method);
        System.out.println("URL: " + url);
        System.out.println("请求头: " + headers);
        System.out.println("参数: " + parameters);
        if (body != null) {
            System.out.println("请求体: " + body);
        }
    }
}

/**
 * HTTP请求建造者
 */
public class HttpRequestBuilder {
    private HttpRequest request = new HttpRequest();

    public HttpRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    public HttpRequestBuilder url(String url) {
        request.setUrl(url);
        return this;
    }

    public HttpRequestBuilder header(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public HttpRequestBuilder parameter(String key, String value) {
        request.addParameter(key, value);
        return this;
    }

    public HttpRequestBuilder body(String body) {
        request.setBody(body);
        return this;
    }

    public HttpRequestBuilder get() {
        return method("GET");
    }

    public HttpRequestBuilder post() {
        return method("POST");
    }

    public HttpRequestBuilder put() {
        return method("PUT");
    }

    public HttpRequestBuilder delete() {
        return method("DELETE");
    }

    public HttpRequest build() {
        return request;
    }
}

// 使用示例
public class HttpRequestBuilderDemo {
    public static void main(String[] args) {
        // GET请求
        HttpRequest getRequest = new HttpRequestBuilder()
                .get()
                .url("https://api.example.com/users")
                .header("Authorization", "Bearer token123")
                .parameter("page", "1")
                .parameter("size", "10")
                .build();

        getRequest.execute();
        System.out.println();

        // POST请求
        HttpRequest postRequest = new HttpRequestBuilder()
                .post()
                .url("https://api.example.com/users")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer token123")
                .body("{\"name\":\"张三\",\"email\":\"zhangsan@example.com\"}")
                .build();

        postRequest.execute();
    }
}
```

## 🎯 建造者模式的变体

### 1. 流畅接口建造者

```java
public class FluentBuilder {
    private Product product = new Product();

    public FluentBuilder withName(String name) {
        product.setName(name);
        return this;
    }

    public FluentBuilder withAge(int age) {
        product.setAge(age);
        return this;
    }

    public Product build() {
        return product;
    }
}
```

### 2. 步骤建造者

```java
public class StepBuilder {
    public static NameStep builder() {
        return new Steps();
    }

    public interface NameStep {
        AgeStep withName(String name);
    }

    public interface AgeStep {
        BuildStep withAge(int age);
    }

    public interface BuildStep {
        Product build();
    }

    private static class Steps implements NameStep, AgeStep, BuildStep {
        private Product product = new Product();

        @Override
        public AgeStep withName(String name) {
            product.setName(name);
            return this;
        }

        @Override
        public BuildStep withAge(int age) {
            product.setAge(age);
            return this;
        }

        @Override
        public Product build() {
            return product;
        }
    }
}
```

---

*建造者模式提供了创建复杂对象的灵活方式，特别适合于参数较多或构建过程复杂的场景。*