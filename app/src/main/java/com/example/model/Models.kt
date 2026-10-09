package com.example.model

import java.util.UUID

enum class ProductCategory(val displayName: String, val iconName: String) {
    ALL("All", "eco"),
    PRODUCE("Organic Produce", "local_florist"),
    PANTRY("Eco Pantry", "shopping_basket"),
    HOME("Zero-Waste Home", "home"),
    FASHION("Sustainable Style", "checkroom"),
    TECH("Solar & Clean Tech", "solar_power"),
    BEAUTY("Natural Beauty", "spa")
}

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val brand: String,
    val category: ProductCategory,
    val price: Double,
    val originalPrice: Double? = null,
    val rating: Double,
    val reviewsCount: Int,
    val ecoScore: String = "A+", // A+, A, B
    val carbonOffsetKg: Double = 1.8,
    val description: String,
    val highlights: List<String> = emptyList(),
    val inStock: Boolean = true,
    val isOrganicCertified: Boolean = true,
    val isFairTrade: Boolean = true,
    val isBestseller: Boolean = false,
    val isDeal: Boolean = false,
    val placeholderColor: Long = 0xFF2E7D32
)

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val author: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val date: String,
    val verifiedPurchase: Boolean = true,
    val helpfulCount: Int = 0,
    val tags: List<String> = emptyList()
)

enum class OrderStatus(val label: String, val stepIndex: Int) {
    CONFIRMED("Order Confirmed", 0),
    PACKED("Packed with Eco-Materials", 1),
    IN_TRANSIT("In Transit (Clean Fleet)", 2),
    OUT_FOR_DELIVERY("Out for Delivery", 3),
    DELIVERED("Delivered Safely", 4)
}

data class OrderTrackingStep(
    val status: OrderStatus,
    val title: String,
    val description: String,
    val time: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class Order(
    val id: String = "GC-" + (1000..9999).random(),
    val orderNumber: String,
    val date: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double = 0.0,
    val pointsRedeemed: Int = 0,
    val pointsRedeemedDiscount: Double = 0.0,
    val shippingFee: Double = 0.0,
    val total: Double,
    val pointsEarned: Int,
    val status: OrderStatus = OrderStatus.IN_TRANSIT,
    val deliverySpeed: String = "Green Express (EV Delivery)",
    val courierName: String = "Alex Rivera",
    val courierVehicle: String = "Eco Electric Cargo Van #104",
    val estimatedDeliveryTime: String = "Today, 3:30 PM",
    val deliveryAddress: String = "742 Evergreen Terrace, Eco District, CA 90210",
    val paymentMethod: String = "Google Pay (Secure)",
    val trackingSteps: List<OrderTrackingStep> = emptyList()
)

enum class LoyaltyTier(val tierName: String, val thresholdPoints: Int, val multiplier: String, val badgeColor: Long) {
    SEEDLING("Seedling", 0, "1x Points", 0xFF66BB6A),
    SPROUT("Sprout", 250, "1.25x Points", 0xFF43A047),
    EVERGREEN("Evergreen VIP", 750, "1.5x Points & Free Eco Shipping", 0xFF1B5E20),
    FOREST_MASTER("Forest Guardian", 1500, "2x Points & Priority EV Fleet", 0xFF004D40)
}

data class LoyaltyProfile(
    val points: Int = 340,
    val totalSpent: Double = 280.50,
    val treesPlanted: Int = 6,
    val currentTier: LoyaltyTier = LoyaltyTier.SPROUT,
    val nextTierPoints: Int = 750
)

enum class ChatSender { USER, AGENT, BOT }

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val timestamp: String,
    val orderRef: String? = null,
    val quickOptions: List<String> = emptyList()
)

enum class AuthProvider(val displayName: String) {
    GOOGLE("Google"),
    APPLE("Apple"),
    PHONE("Phone SMS"),
    EMAIL("Email"),
    GUEST("Guest Account")
}

data class UserProfile(
    val name: String = "Maya Lin",
    val email: String = "maya.lin@greencart.org",
    val phone: String = "+1 (555) 234-8901",
    val avatarInitials: String = "ML",
    val authProvider: AuthProvider = AuthProvider.GOOGLE,
    val primaryAddress: String = "742 Evergreen Terrace, Eco District, CA 90210"
)

enum class AppLanguage(val code: String, val nativeName: String, val flag: String) {
    ENGLISH("en", "English", "🇺🇸"),
    SPANISH("es", "Español", "🇪🇸"),
    FRENCH("fr", "Français", "🇫🇷"),
    GERMAN("de", "Deutsch", "🇩🇪"),
    ARABIC("ar", "العربية", "🇸🇦"),
    JAPANESE("ja", "日本語", "🇯🇵")
}

data class PushNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val orderId: String? = null
)
