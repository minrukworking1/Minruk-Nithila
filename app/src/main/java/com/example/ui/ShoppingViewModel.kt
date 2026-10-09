package com.example.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.AppLanguage
import com.example.model.AuthProvider
import com.example.model.CartItem
import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.LoyaltyProfile
import com.example.model.LoyaltyTier
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.OrderTrackingStep
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.PushNotification
import com.example.model.Review
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppScreen {
    HOME,
    SEARCH,
    WISHLIST,
    CART,
    ORDER_TRACKING,
    LOYALTY_REWARDS,
    CUSTOMER_SUPPORT,
    PROFILE
}

enum class SortOption(val label: String) {
    POPULARITY("Most Popular"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Highest Rated"),
    ECO_SCORE("Highest Eco-Score")
}

data class ShoppingUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val products: List<Product> = SampleData.sampleProducts,
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val searchQuery: String = "",
    val searchHistory: List<String> = listOf("Avocados", "Bamboo", "Matcha", "Olive Oil"),
    val sortOption: SortOption = SortOption.POPULARITY,
    val filterOrganicOnly: Boolean = false,
    val filterDealsOnly: Boolean = false,
    val recentlyViewedProductIds: List<String> = listOf("prod-1", "prod-3", "prod-9"),
    
    // Cart & Wishlist
    val cartItems: List<CartItem> = listOf(
        CartItem(SampleData.sampleProducts[0], 2),
        CartItem(SampleData.sampleProducts[2], 1)
    ),
    val wishlistProductIds: Set<String> = setOf("prod-4", "prod-8"),
    val appliedPromoCode: String? = null,
    val promoDiscountPercent: Double = 0.0,
    val redeemPointsChecked: Boolean = false,
    
    // Active / Selected Product
    val viewingProduct: Product? = null,
    
    // Reviews
    val reviewsByProduct: Map<String, List<Review>> = mapOf(
        "prod-1" to SampleData.sampleReviews.filter { it.productId == "prod-1" },
        "prod-2" to SampleData.sampleReviews.filter { it.productId == "prod-2" },
        "prod-3" to SampleData.sampleReviews.filter { it.productId == "prod-3" }
    ),
    val showWriteReviewDialog: Boolean = false,
    
    // Checkout & Order
    val isCheckoutSheetOpen: Boolean = false,
    val checkoutStep: Int = 1, // 1: Address, 2: Shipping, 3: Payment, 4: Review
    val selectedAddress: String = "742 Evergreen Terrace, Eco District, CA 90210",
    val selectedShippingMethod: String = "Green Express (EV Delivery - Carbon Free)",
    val shippingCost: Double = 0.0,
    val selectedPaymentMethod: String = "Google Pay (Secure 1-Tap)",
    val cardNumber: String = "•••• •••• •••• 4242",
    val cardExpiry: String = "12/28",
    val cardCvv: String = "•••",
    val isProcessingPayment: Boolean = false,
    
    // Order Tracking
    val activeOrder: Order = SampleData.initialSampleOrder,
    val orderHistory: List<Order> = listOf(SampleData.initialSampleOrder),
    
    // Loyalty Program
    val loyaltyProfile: LoyaltyProfile = LoyaltyProfile(
        points = 450,
        totalSpent = 312.40,
        treesPlanted = 7,
        currentTier = LoyaltyTier.SPROUT,
        nextTierPoints = 750
    ),
    
    // Customer Support Chat
    val chatMessages: List<ChatMessage> = SampleData.initialChatMessages,
    val isSupportTyping: Boolean = false,
    
    // User Profile & Authentication
    val userProfile: UserProfile = UserProfile(),
    val isAuthSheetOpen: Boolean = false,
    val isLanguageSheetOpen: Boolean = false,
    val isNotificationsSheetOpen: Boolean = false,
    
    // Push Notifications
    val notifications: List<PushNotification> = SampleData.initialNotifications,
    val unreadNotificationsCount: Int = 2,
    
    // In-app Alert / Toast banner
    val snackbarMessage: String? = null
)

class ShoppingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(selectedLanguage = lang, isLanguageSheetOpen = false) }
        showSnackbar("Language switched to ${lang.nativeName}")
    }

    // --- Search & Filters ---
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun executeSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            _uiState.update { state ->
                val updatedHistory = (listOf(trimmed) + state.searchHistory.filter { it != trimmed }).take(6)
                state.copy(searchQuery = trimmed, searchHistory = updatedHistory)
            }
        }
    }

    fun selectCategory(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSortOption(sort: SortOption) {
        _uiState.update { it.copy(sortOption = sort) }
    }

    fun toggleOrganicFilter() {
        _uiState.update { it.copy(filterOrganicOnly = !it.filterOrganicOnly) }
    }

    fun toggleDealsFilter() {
        _uiState.update { it.copy(filterDealsOnly = !it.filterDealsOnly) }
    }

    // --- Product Selection & Recently Viewed ---
    fun viewProductDetails(product: Product) {
        _uiState.update { state ->
            val updatedRecent = (listOf(product.id) + state.recentlyViewedProductIds.filter { it != product.id }).take(8)
            state.copy(viewingProduct = product, recentlyViewedProductIds = updatedRecent)
        }
    }

    fun closeProductDetails() {
        _uiState.update { it.copy(viewingProduct = null) }
    }

    // --- Wishlist ---
    fun toggleWishlist(productId: String) {
        _uiState.update { state ->
            val updated = state.wishlistProductIds.toMutableSet()
            val added: Boolean
            if (updated.contains(productId)) {
                updated.remove(productId)
                added = false
            } else {
                updated.add(productId)
                added = true
            }
            state.copy(
                wishlistProductIds = updated,
                snackbarMessage = if (added) "Saved to your Wishlist ❤️" else "Removed from Wishlist"
            )
        }
    }

    // --- Cart ---
    fun addToCart(product: Product, quantity: Int = 1) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.product.id == product.id }
            val updatedList = if (existing != null) {
                state.cartItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it
                }
            } else {
                state.cartItems + CartItem(product, quantity)
            }
            state.copy(
                cartItems = updatedList,
                snackbarMessage = "Added ${product.name} to cart 🛒"
            )
        }
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.product.id == productId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else {
                    item
                }
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeFromCart(productId: String) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.filterNot { it.product.id == productId })
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    fun moveWishlistItemToCart(product: Product) {
        addToCart(product)
        toggleWishlist(product.id)
    }

    fun applyPromoCode(code: String) {
        val upper = code.trim().uppercase()
        if (upper == "ECO20" || upper == "GREEN20") {
            _uiState.update { it.copy(appliedPromoCode = upper, promoDiscountPercent = 0.20, snackbarMessage = "Promo code $upper applied: 20% OFF! 🎉") }
        } else if (upper == "EARTH10") {
            _uiState.update { it.copy(appliedPromoCode = upper, promoDiscountPercent = 0.10, snackbarMessage = "Promo code $upper applied: 10% OFF! 🌱") }
        } else {
            showSnackbar("Invalid or expired code. Try 'ECO20' or 'EARTH10'")
        }
    }

    fun toggleRedeemPoints() {
        _uiState.update { it.copy(redeemPointsChecked = !it.redeemPointsChecked) }
    }

    // --- Checkout Flow ---
    fun openCheckout() {
        if (_uiState.value.cartItems.isEmpty()) {
            showSnackbar("Your cart is empty!")
            return
        }
        _uiState.update { it.copy(isCheckoutSheetOpen = true, checkoutStep = 1) }
    }

    fun closeCheckout() {
        _uiState.update { it.copy(isCheckoutSheetOpen = false, isProcessingPayment = false) }
    }

    fun setCheckoutStep(step: Int) {
        _uiState.update { it.copy(checkoutStep = step) }
    }

    fun selectAddress(address: String) {
        _uiState.update { it.copy(selectedAddress = address) }
    }

    fun selectShippingMethod(method: String, cost: Double) {
        _uiState.update { it.copy(selectedShippingMethod = method, shippingCost = cost) }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun placeOrder(context: Context? = null) {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        _uiState.update { it.copy(isProcessingPayment = true) }

        viewModelScope.launch {
            delay(1500) // Simulated secure payment gateway tokenization & verification
            
            val subtotal = state.cartItems.sumOf { it.product.price * it.quantity }
            val promoDiscount = subtotal * state.promoDiscountPercent
            val pointsToRedeem = if (state.redeemPointsChecked) minOf(state.loyaltyProfile.points, (subtotal * 100).toInt()) else 0
            val pointsDiscount = pointsToRedeem / 100.0
            val total = maxOf(0.0, subtotal - promoDiscount - pointsDiscount + state.shippingCost)
            val pointsEarned = (total * 10).toInt()

            val orderId = "GC-" + (1000..9999).random()
            val newOrder = Order(
                id = orderId,
                orderNumber = "#$orderId",
                date = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(Date()),
                items = state.cartItems,
                subtotal = subtotal,
                discount = promoDiscount,
                pointsRedeemed = pointsToRedeem,
                pointsRedeemedDiscount = pointsDiscount,
                shippingFee = state.shippingCost,
                total = total,
                pointsEarned = pointsEarned,
                status = OrderStatus.CONFIRMED,
                deliverySpeed = state.selectedShippingMethod,
                deliveryAddress = state.selectedAddress,
                paymentMethod = state.selectedPaymentMethod,
                trackingSteps = listOf(
                    OrderTrackingStep(OrderStatus.CONFIRMED, "Order Confirmed & Payment Verified", "256-bit encrypted transaction processed successfully.", "Just now", isCompleted = true, isCurrent = true),
                    OrderTrackingStep(OrderStatus.PACKED, "Eco-Friendly Packaging Hub", "Items will be packed in 100% recycled paper & plastic-free cushioning.", "Pending", isCompleted = false, isCurrent = false),
                    OrderTrackingStep(OrderStatus.IN_TRANSIT, "Clean Electric Fleet Dispatch", "EV Cargo courier dispatch scheduled.", "Pending", isCompleted = false, isCurrent = false),
                    OrderTrackingStep(OrderStatus.OUT_FOR_DELIVERY, "Local Delivery Driver", "Final mile carbon-neutral delivery.", "Pending", isCompleted = false, isCurrent = false),
                    OrderTrackingStep(OrderStatus.DELIVERED, "Delivered Safely", "Zero-contact doorstep delivery.", "Pending", isCompleted = false, isCurrent = false)
                )
            )

            // Update loyalty points
            val updatedPoints = state.loyaltyProfile.points - pointsToRedeem + pointsEarned
            val updatedTotalSpent = state.loyaltyProfile.totalSpent + total
            val updatedTrees = (updatedTotalSpent / 40.0).toInt()
            val newTier = when {
                updatedPoints >= LoyaltyTier.FOREST_MASTER.thresholdPoints -> LoyaltyTier.FOREST_MASTER
                updatedPoints >= LoyaltyTier.EVERGREEN.thresholdPoints -> LoyaltyTier.EVERGREEN
                updatedPoints >= LoyaltyTier.SPROUT.thresholdPoints -> LoyaltyTier.SPROUT
                else -> LoyaltyTier.SEEDLING
            }

            // Create notification
            val orderNotification = PushNotification(
                title = "Order $newOrder.orderNumber Placed! 📦",
                message = "Thank you! You earned $pointsEarned GreenPoints and offset ${(subtotal * 0.15).toInt()}kg CO₂.",
                timeAgo = "Just now",
                orderId = newOrder.id
            )

            // Trigger system push notification if context provided
            context?.let { ctx ->
                sendSystemNotification(
                    ctx,
                    "Order #${newOrder.id} Confirmed! 🌿",
                    "Payment confirmed! Electric delivery van assigned. Track live."
                )
            }

            _uiState.update { current ->
                current.copy(
                    cartItems = emptyList(),
                    isProcessingPayment = false,
                    isCheckoutSheetOpen = false,
                    activeOrder = newOrder,
                    orderHistory = listOf(newOrder) + current.orderHistory,
                    loyaltyProfile = current.loyaltyProfile.copy(
                        points = updatedPoints,
                        totalSpent = updatedTotalSpent,
                        treesPlanted = updatedTrees,
                        currentTier = newTier
                    ),
                    notifications = listOf(orderNotification) + current.notifications,
                    unreadNotificationsCount = current.unreadNotificationsCount + 1,
                    currentScreen = AppScreen.ORDER_TRACKING,
                    snackbarMessage = "Order placed successfully! Tracking is live 🚀"
                )
            }
        }
    }

    // --- Real-time Order Tracking Simulation ---
    fun advanceOrderTrackingStage() {
        _uiState.update { state ->
            val order = state.activeOrder
            val nextStepIndex = (order.status.stepIndex + 1).coerceAtMost(OrderStatus.DELIVERED.stepIndex)
            val nextStatus = OrderStatus.values().find { it.stepIndex == nextStepIndex } ?: OrderStatus.DELIVERED

            val updatedSteps = order.trackingSteps.map { step ->
                when {
                    step.status.stepIndex < nextStepIndex -> step.copy(isCompleted = true, isCurrent = false)
                    step.status.stepIndex == nextStepIndex -> step.copy(isCompleted = true, isCurrent = true, time = "Just now")
                    else -> step.copy(isCompleted = false, isCurrent = false)
                }
            }

            val updatedOrder = order.copy(status = nextStatus, trackingSteps = updatedSteps)
            val updatedHistory = state.orderHistory.map { if (it.id == order.id) updatedOrder else it }

            val statusNotification = PushNotification(
                title = "Order ${order.orderNumber} Update: ${nextStatus.label}",
                message = "Live location updated. Courier moving towards destination.",
                timeAgo = "Just now",
                orderId = order.id
            )

            state.copy(
                activeOrder = updatedOrder,
                orderHistory = updatedHistory,
                notifications = listOf(statusNotification) + state.notifications,
                unreadNotificationsCount = state.unreadNotificationsCount + 1,
                snackbarMessage = "Updated tracking: ${nextStatus.label} 🚚"
            )
        }
    }

    // --- Reviews ---
    fun openWriteReviewDialog(product: Product) {
        _uiState.update { it.copy(viewingProduct = product, showWriteReviewDialog = true) }
    }

    fun closeWriteReviewDialog() {
        _uiState.update { it.copy(showWriteReviewDialog = false) }
    }

    fun submitReview(productId: String, rating: Int, title: String, comment: String, tags: List<String>) {
        val newReview = Review(
            productId = productId,
            author = _uiState.value.userProfile.name,
            rating = rating,
            title = title,
            comment = comment,
            date = "Today",
            verifiedPurchase = true,
            helpfulCount = 0,
            tags = tags
        )

        _uiState.update { state ->
            val currentList = state.reviewsByProduct[productId] ?: emptyList()
            val updatedMap = state.reviewsByProduct.toMutableMap()
            updatedMap[productId] = listOf(newReview) + currentList

            // Also reward points for writing review
            val updatedLoyalty = state.loyaltyProfile.copy(points = state.loyaltyProfile.points + 25)

            state.copy(
                reviewsByProduct = updatedMap,
                showWriteReviewDialog = false,
                loyaltyProfile = updatedLoyalty,
                snackbarMessage = "Review published! +25 GreenPoints earned ⭐"
            )
        }
    }

    fun markReviewHelpful(productId: String, reviewId: String) {
        _uiState.update { state ->
            val currentList = state.reviewsByProduct[productId] ?: return@update state
            val updatedList = currentList.map {
                if (it.id == reviewId) it.copy(helpfulCount = it.helpfulCount + 1) else it
            }
            val updatedMap = state.reviewsByProduct.toMutableMap()
            updatedMap[productId] = updatedList
            state.copy(reviewsByProduct = updatedMap, snackbarMessage = "Marked review as helpful 👍")
        }
    }

    // --- Customer Support Chat ---
    fun sendChatMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return

        val userMsg = ChatMessage(
            sender = ChatSender.USER,
            text = trimmed,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        )

        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + userMsg,
                isSupportTyping = true
            )
        }

        viewModelScope.launch {
            delay(1200) // Realistic typing simulation
            val replyText = generateBotReply(trimmed, _uiState.value)
            val botMsg = ChatMessage(
                sender = if (trimmed.contains("agent", ignoreCase = true)) ChatSender.AGENT else ChatSender.BOT,
                text = replyText,
                timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
                orderRef = if (trimmed.contains("order", ignoreCase = true)) _uiState.value.activeOrder.orderNumber else null,
                quickOptions = listOf("Track my delivery", "What is my GreenPoints balance?", "Return policy", "Zero-waste packaging")
            )

            _uiState.update { state ->
                state.copy(
                    chatMessages = state.chatMessages + botMsg,
                    isSupportTyping = false
                )
            }
        }
    }

    private fun generateBotReply(input: String, state: ShoppingUiState): String {
        val lower = input.lowercase()
        return when {
            lower.contains("track") || lower.contains("order") || lower.contains("where") -> {
                "Your active order ${state.activeOrder.orderNumber} is currently at: \"${state.activeOrder.status.label}\". Delivery courier ${state.activeOrder.courierName} (${state.activeOrder.courierVehicle}) is estimated to arrive at ${state.activeOrder.estimatedDeliveryTime}."
            }
            lower.contains("point") || lower.contains("reward") || lower.contains("loyalty") -> {
                "You currently have ${state.loyaltyProfile.points} GreenPoints (worth \$${String.format(Locale.US, "%.2f", state.loyaltyProfile.points / 100.0)} off). Your current tier is ${state.loyaltyProfile.currentTier.tierName}. You have helped plant ${state.loyaltyProfile.treesPlanted} trees!"
            }
            lower.contains("agent") || lower.contains("human") || lower.contains("speak") -> {
                "Hi! I'm Sarah from GreenCart Customer Care. I have connected to your active session and can help with any questions regarding your delivery or account."
            }
            lower.contains("return") || lower.contains("refund") -> {
                "We provide a 30-day hassle-free eco return policy. Simply bring items in original compostable packaging or request a zero-emission courier pickup through your orders page."
            }
            lower.contains("packag") || lower.contains("waste") -> {
                "100% of our orders are shipped using Forest Stewardship Council (FSC) certified kraft cartons, water-activated paper tape, and biodegradable cornstarch loose fill. Zero virgin plastics!"
            }
            else -> {
                "Thank you for contacting GreenCart Support! I've noted: \"$input\". Our team strives for 100% customer happiness and climate sustainability. Would you like to check an order status or talk with a live specialist?"
            }
        }
    }

    // --- Authentication & Profile ---
    fun openAuthSheet() {
        _uiState.update { it.copy(isAuthSheetOpen = true) }
    }

    fun closeAuthSheet() {
        _uiState.update { it.copy(isAuthSheetOpen = false) }
    }

    fun loginWithSocial(provider: AuthProvider) {
        val sampleUser = when (provider) {
            AuthProvider.GOOGLE -> UserProfile("Maya Lin", "maya.lin@gmail.com", "+1 555-0199", "ML", AuthProvider.GOOGLE)
            AuthProvider.APPLE -> UserProfile("Maya Lin", "maya.apple@icloud.com", "+1 555-0199", "ML", AuthProvider.APPLE)
            AuthProvider.PHONE -> UserProfile("Mobile User", "user.sms@greencart.org", "+1 555-7821", "MU", AuthProvider.PHONE)
            AuthProvider.EMAIL -> UserProfile("Maya Green", "maya@greencart.org", "+1 555-4321", "MG", AuthProvider.EMAIL)
            AuthProvider.GUEST -> UserProfile("Eco Guest", "guest@greencart.org", "+1 555-0000", "EG", AuthProvider.GUEST)
        }
        _uiState.update {
            it.copy(
                userProfile = sampleUser,
                isAuthSheetOpen = false,
                snackbarMessage = "Signed in with ${provider.displayName} 🌿"
            )
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                userProfile = UserProfile("Eco Guest", "guest@greencart.org", "+1 555-0000", "EG", AuthProvider.GUEST),
                snackbarMessage = "Signed out"
            )
        }
    }

    // --- Notifications ---
    fun toggleNotificationsSheet() {
        _uiState.update { it.copy(isNotificationsSheetOpen = !it.isNotificationsSheetOpen) }
    }

    fun toggleLanguageSheet() {
        _uiState.update { it.copy(isLanguageSheetOpen = !it.isLanguageSheetOpen) }
    }

    fun markNotificationsAsRead() {
        _uiState.update { state ->
            val updated = state.notifications.map { it.copy(isRead = true) }
            state.copy(notifications = updated, unreadNotificationsCount = 0)
        }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    private fun sendSystemNotification(context: Context, title: String, content: String) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "greencart_order_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "GreenCart Order & Delivery Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Instant notifications for order statuses and eco-rewards"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify((1000..9999).random(), notification)
        } catch (_: Exception) {
            // Gracefully handled if permission not yet granted
        }
    }
}
