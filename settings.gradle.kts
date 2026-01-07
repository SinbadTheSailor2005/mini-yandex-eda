rootProject.name = "order-platform"

include(
    "shared-libs",
    "delivery-service",
    "order-service",
    "payment-service",
)
include("warehouse-service")
include("payment-service:untitled")
include("warehouse-api")
include("shared-libs")