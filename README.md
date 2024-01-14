# spring-boot-otel-context-propagation
## Introduction
In my previous [spring-boot-otel-jaeger](https://github.com/santipabWannakiri/spring-boot-otel-jaeger) POC project, I utilized Spring Boot with OpenTelemetry, incorporating the configuration of the Otel Collector and Jaeger dashboards. This project provides a comprehensive overview of the advantages of adopting OpenTelemetry, particularly in the realm of tracing. While it successfully establishes the foundation for understanding OpenTelemetry, there is still a crucial core concept that requires attention – `Context Propagation`. Consequently, my focus for this POC project will be on the seamless setup of the end-to-end project, followed by a demonstration of how context propagation operates and its impact on the overall results.

## OpenTelemetry context propagation
<p align="center">
  <img src="images/ex-cart-product-service.png" alt="image description" width="800" height="300">
</p>

### Interaction Between Cart and Product Services
The presented illustration depicts a collaborative interaction between two services: Cart and Product. The sequence begins with the following steps:

#### 1. User Clicks "Add to Cart"
   - The user initiates the action by clicking "Add to Cart."

#### 2. Cart Service Requests Product Info
   - Cart service calls Product service to retrieve product information (HTTP GET).
   - Product service responds with product details.
   - Cart service internally validates the received product information.

#### 3. Deduct Quantity Request
   - If the validation is successful, Cart service requests quantity deduction from Product service (HTTP PUT).
   - Product service deducts the quantity and responds.
   - Cart service updates the cart based on the response.
   - Cart service notifies the user of success/failure.




[W3C Trace Context](https://engineering.dynatrace.com/open-source/standards/w3c-trace-context/)\
[What is OpenTelemetry? A Straightforward Guide](https://www.aspecto.io/blog/what-is-opentelemetry-the-infinitive-guide/)
