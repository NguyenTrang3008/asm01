# ASSIGNMENT 01: SOLID PRINCIPLES REFACTORING

## Phân tích vấn đề

### Vấn đề ban đầu
Class `OrderService` ban đầu là một "God Class" điển hình với các vấn đề sau:

1. **Vi phạm SRP (Single Responsibility Principle)**
   - Class xử lý quá nhiều trách nhiệm: validation, pricing, discount, payment, stock, notification, logging, analytics
   - Mỗi thay đổi trong bất kỳ chức năng nào đều phải sửa class này

2. **Vi phạm OCP (Open/Closed Principle)**
   - Thêm discount mới hoặc payment method mới phải sửa code hiện tại (if-else chains)
   - Không thể mở rộng mà không sửa đổi

3. **Vi phạm DIP (Dependency Inversion Principle)**
   - Phụ thuộc trực tiếp vào implementation cụ thể
   - Không có abstraction layer

4. **Vi phạm ISP (Interface Segregation Principle)**
   - Không có interface, client phải phụ thuộc vào toàn bộ class lớn

5. **Thiếu type safety**
   - Sử dụng `Object[]` arrays thay vì domain models
   - Dễ gây lỗi runtime, khó maintain

## Giải pháp áp dụng SOLID

### 1. Single Responsibility Principle (SRP)

Tách `OrderService` thành nhiều class, mỗi class có một trách nhiệm duy nhất:

- **Domain Models**: `Product`, `Order`, `OrderItem`, `OrderStatus`
  - Chịu trách nhiệm: Đại diện cho business entities với type safety

- **Repositories**: `ProductRepository`, `OrderRepository`
  - Chịu trách nhiệm: Quản lý data access và persistence

- **DiscountService**
  - Chịu trách nhiệm: Tính toán giá sau discount

- **PaymentService**
  - Chịu trách nhiệm: Xử lý payment và tính phí

- **StockManager**
  - Chịu trách nhiệm: Quản lý inventory (reserve/release stock)

- **NotificationService** (EmailNotificationService, SmsNotificationService)
  - Chịu trách nhiệm: Gửi thông báo

- **LoggingService**
  - Chịu trách nhiệm: Logging

- **AnalyticsService**
  - Chịu trách nhiệm: Tracking analytics

- **OrderService** (refactored)
  - Chịu trách nhiệm: Orchestrate order workflow, coordinate các services

### 2. Open/Closed Principle (OCP)

**Discount Strategy Pattern:**
```java
interface DiscountStrategy {
    double applyDiscount(Product product);
}
```

Implementations:
- `ElectronicsDiscountStrategy`: 5% off cho electronics > $500
- `ClothingDiscountStrategy`: 10% off cho clothing
- `NoDiscountStrategy`: Không discount

**Lợi ích:**
- Thêm discount mới (ví dụ: Black Friday 20% off) chỉ cần tạo class mới implement `DiscountStrategy`
- Không cần sửa code hiện tại
- Dễ dàng test từng strategy riêng biệt

**Payment Strategy Pattern:**
```java
interface PaymentProcessor {
    boolean processPayment(double amount);
    double calculateFee(double amount);
}
```

Implementations:
- `CreditCardPaymentProcessor`: 3% fee, 90% success rate
- `PayPalPaymentProcessor`: 2.5% fee, 95% success rate
- `BankTransferPaymentProcessor`: No fee, 100% success

**Lợi ích:**
- Thêm payment method mới (ví dụ: Crypto) chỉ cần implement interface
- Không sửa `PaymentService` hay `OrderService`

### 3. Liskov Substitution Principle (LSP)

Tất cả implementations của `DiscountStrategy` và `PaymentProcessor` đều có thể thay thế cho nhau mà không làm hỏng hệ thống:

```java
// Có thể swap bất kỳ implementation nào
DiscountStrategy strategy = new ElectronicsDiscountStrategy();
strategy = new ClothingDiscountStrategy(); // LSP satisfied

PaymentProcessor processor = new CreditCardPaymentProcessor();
processor = new PayPalPaymentProcessor(); // LSP satisfied
```

### 4. Interface Segregation Principle (ISP)

Thay vì một interface lớn, tạo các interface nhỏ, focused:

- `DiscountStrategy`: Chỉ có `applyDiscount()`
- `PaymentProcessor`: Chỉ có `processPayment()` và `calculateFee()`
- `NotificationService`: Chỉ có `sendNotification()`

Mỗi client chỉ phụ thuộc vào interface mà nó cần, không bị ép phải implement methods không dùng.

### 5. Dependency Inversion Principle (DIP)

**Trước khi refactor:**
```java
public class OrderService {
    // Phụ thuộc trực tiếp vào implementation
    private List<Object[]> products = new ArrayList<>();
}
```

**Sau khi refactor:**
```java
public class OrderService {
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final NotificationService emailService;
    // ... inject dependencies qua constructor
    
    public OrderService(ProductRepository productRepository,
                        PaymentService paymentService,
                        NotificationService emailService, ...) {
        // Dependency Injection
    }
}
```

**Lợi ích:**
- High-level module (`OrderService`) không phụ thuộc vào low-level modules
- Cả hai phụ thuộc vào abstractions (interfaces)
- Dễ dàng test với mock objects
- Dễ dàng swap implementations

## Kết quả

### Trước refactoring:
- 1 class với 150+ lines
- Khó test, khó maintain
- Thêm feature mới phải sửa nhiều chỗ
- Không có type safety

### Sau refactoring:
- 20+ classes, mỗi class < 50 lines
- Mỗi class có trách nhiệm rõ ràng
- Dễ test từng component riêng
- Thêm feature mới không cần sửa code cũ
- Type-safe với domain models
- Tuân thủ tất cả SOLID principles

## Cách chạy

```bash
cd demo/assignment01

# Compile tất cả packages
javac -d out src/main/java/com/ecommerce/*.java \
             src/main/java/com/ecommerce/model/*.java \
             src/main/java/com/ecommerce/repository/*.java \
             src/main/java/com/ecommerce/service/*.java \
             src/main/java/com/ecommerce/service/discount/*.java \
             src/main/java/com/ecommerce/service/payment/*.java \
             src/main/java/com/ecommerce/service/notification/*.java

# Chạy
java -cp out com.ecommerce.Main
```

## Package Organization

Code được tổ chức theo packages rõ ràng:

- `com.ecommerce.model` - Domain models (Product, Order, OrderItem, OrderStatus)
- `com.ecommerce.repository` - Data access layer (ProductRepository, OrderRepository)
- `com.ecommerce.service` - Business services (OrderService, DiscountService, PaymentService, etc.)
  - `service.discount` - Discount strategies
  - `service.payment` - Payment processors
  - `service.notification` - Notification services

Xem chi tiết trong [STRUCTURE.md](./STRUCTURE.md)

## Mở rộng trong tương lai

### Thêm discount mới (Black Friday):
```java
package com.ecommerce.service.discount;

import com.ecommerce.model.Product;

public class BlackFridayDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(Product product) {
        return product.getPrice() * 0.80; // 20% off everything
    }
}
```

Sau đó thêm vào `DiscountService`:
```java
strategies.add(new BlackFridayDiscountStrategy());
```

### Thêm payment method mới (Crypto):
```java
package com.ecommerce.service.payment;

public class CryptoPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing crypto payment...");
        return true;
    }
    
    @Override
    public double calculateFee(double amount) {
        return amount * 0.01; // 1% crypto fee
    }
}
```

Sau đó register vào `PaymentService`:
```java
processors.put("CRYPTO", new CryptoPaymentProcessor());
```

Chỉ cần tạo class mới trong đúng package, không cần sửa logic khác!
