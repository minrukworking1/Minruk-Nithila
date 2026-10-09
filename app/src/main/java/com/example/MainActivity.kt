package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.ShoppingViewModel
import com.example.ui.components.GreenCartBottomNav
import com.example.ui.components.GreenCartNavigationRail
import com.example.ui.components.GreenCartTopBar
import com.example.ui.components.LanguagePickerSheet
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.SocialAuthSheet
import com.example.ui.components.WriteReviewDialog
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutSheet
import com.example.ui.screens.CustomerSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoyaltyRewardsScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailSheet
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.GreenCartTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ShoppingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenCartTheme {
                GreenCartApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GreenCartApp(viewModel: ShoppingViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle snackbar messages
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button on secondary screens
    if (state.currentScreen != AppScreen.HOME) {
        BackHandler {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    // Responsive container: Check if tablet/wide screen (>= 600dp)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                GreenCartTopBar(
                    currentLanguage = state.selectedLanguage,
                    unreadNotificationsCount = state.unreadNotificationsCount,
                    loyaltyPoints = state.loyaltyProfile.points,
                    userProfile = state.userProfile,
                    onLanguageClick = { viewModel.toggleLanguageSheet() },
                    onNotificationsClick = { viewModel.toggleNotificationsSheet() },
                    onLoyaltyClick = { viewModel.navigateTo(AppScreen.LOYALTY_REWARDS) },
                    onProfileClick = { viewModel.navigateTo(AppScreen.PROFILE) }
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    GreenCartBottomNav(
                        currentScreen = state.currentScreen,
                        cartCount = state.cartItems.sumOf { it.quantity },
                        wishlistCount = state.wishlistProductIds.size,
                        language = state.selectedLanguage,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Tablet navigation rail on wide screens
                if (isWideScreen) {
                    GreenCartNavigationRail(
                        currentScreen = state.currentScreen,
                        cartCount = state.cartItems.sumOf { it.quantity },
                        wishlistCount = state.wishlistProductIds.size,
                        language = state.selectedLanguage,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                // Active Screen Body
                Box(modifier = Modifier.weight(1f)) {
                    when (state.currentScreen) {
                        AppScreen.HOME -> {
                            HomeScreen(
                                state = state,
                                onNavigate = { viewModel.navigateTo(it) },
                                onProductClick = { viewModel.viewProductDetails(it) },
                                onAddToCart = { viewModel.addToCart(it) },
                                onToggleWishlist = { viewModel.toggleWishlist(it) },
                                onCategoryClick = { viewModel.selectCategory(it) }
                            )
                        }

                        AppScreen.SEARCH -> {
                            SearchScreen(
                                state = state,
                                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                                onExecuteSearch = { viewModel.executeSearch(it) },
                                onCategorySelect = { viewModel.selectCategory(it) },
                                onSortSelect = { viewModel.setSortOption(it) },
                                onToggleOrganic = { viewModel.toggleOrganicFilter() },
                                onToggleDeals = { viewModel.toggleDealsFilter() },
                                onProductClick = { viewModel.viewProductDetails(it) },
                                onAddToCart = { viewModel.addToCart(it) },
                                onToggleWishlist = { viewModel.toggleWishlist(it) }
                            )
                        }

                        AppScreen.WISHLIST -> {
                            WishlistScreen(
                                state = state,
                                onNavigate = { viewModel.navigateTo(it) },
                                onProductClick = { viewModel.viewProductDetails(it) },
                                onMoveToCart = { viewModel.moveWishlistItemToCart(it) },
                                onRemoveFromWishlist = { viewModel.toggleWishlist(it) },
                                onShareWishlist = { viewModel.showSnackbar("Wishlist link copied to clipboard!") }
                            )
                        }

                        AppScreen.CART -> {
                            CartScreen(
                                state = state,
                                onNavigate = { viewModel.navigateTo(it) },
                                onQuantityChange = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                                onRemoveItem = { viewModel.removeFromCart(it) },
                                onClearCart = { viewModel.clearCart() },
                                onApplyPromo = { viewModel.applyPromoCode(it) },
                                onToggleRedeemPoints = { viewModel.toggleRedeemPoints() },
                                onOpenCheckout = { viewModel.openCheckout() }
                            )
                        }

                        AppScreen.ORDER_TRACKING -> {
                            OrderTrackingScreen(
                                state = state,
                                onAdvanceStage = { viewModel.advanceOrderTrackingStage() }
                            )
                        }

                        AppScreen.LOYALTY_REWARDS -> {
                            LoyaltyRewardsScreen(
                                state = state,
                                onRedeemVoucher = { voucher, cost ->
                                    viewModel.showSnackbar("Redeemed '$voucher' for $cost GreenPoints! 🎉")
                                }
                            )
                        }

                        AppScreen.CUSTOMER_SUPPORT -> {
                            CustomerSupportScreen(
                                state = state,
                                onSendMessage = { viewModel.sendChatMessage(it) }
                            )
                        }

                        AppScreen.PROFILE -> {
                            ProfileScreen(
                                state = state,
                                onNavigate = { viewModel.navigateTo(it) },
                                onOpenAuthSheet = { viewModel.openAuthSheet() },
                                onOpenLanguageSheet = { viewModel.toggleLanguageSheet() },
                                onLogout = { viewModel.logout() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Sheets & Dialogs
    // 1. Product Detail Sheet
    state.viewingProduct?.let { product ->
        val reviews = state.reviewsByProduct[product.id] ?: emptyList()
        ProductDetailSheet(
            product = product,
            reviews = reviews,
            isWishlisted = state.wishlistProductIds.contains(product.id),
            onDismiss = { viewModel.closeProductDetails() },
            onAddToCart = { prod, qty -> viewModel.addToCart(prod, qty) },
            onToggleWishlist = { viewModel.toggleWishlist(it) },
            onOpenWriteReview = { viewModel.openWriteReviewDialog(it) },
            onMarkReviewHelpful = { prodId, revId -> viewModel.markReviewHelpful(prodId, revId) }
        )
    }

    // 2. Checkout Modal Sheet
    if (state.isCheckoutSheetOpen) {
        CheckoutSheet(
            state = state,
            onDismiss = { viewModel.closeCheckout() },
            onStepChange = { viewModel.setCheckoutStep(it) },
            onSelectAddress = { viewModel.selectAddress(it) },
            onSelectShipping = { method, fee -> viewModel.selectShippingMethod(method, fee) },
            onSelectPayment = { viewModel.selectPaymentMethod(it) },
            onPlaceOrder = { ctx -> viewModel.placeOrder(ctx) }
        )
    }

    // 3. Social Media Login Sheet
    if (state.isAuthSheetOpen) {
        SocialAuthSheet(
            onDismiss = { viewModel.closeAuthSheet() },
            onSelectProvider = { viewModel.loginWithSocial(it) }
        )
    }

    // 4. Language Picker Sheet
    if (state.isLanguageSheetOpen) {
        LanguagePickerSheet(
            currentLanguage = state.selectedLanguage,
            onSelectLanguage = { viewModel.setLanguage(it) },
            onDismiss = { viewModel.toggleLanguageSheet() }
        )
    }

    // 5. Notifications Sheet
    if (state.isNotificationsSheetOpen) {
        NotificationsSheet(
            notifications = state.notifications,
            onDismiss = { viewModel.toggleNotificationsSheet() },
            onMarkAllRead = { viewModel.markNotificationsAsRead() },
            onNotificationClick = { notif ->
                if (notif.orderId != null) {
                    viewModel.navigateTo(AppScreen.ORDER_TRACKING)
                }
            }
        )
    }

    // 6. Write Customer Review Dialog
    if (state.showWriteReviewDialog && state.viewingProduct != null) {
        WriteReviewDialog(
            product = state.viewingProduct!!,
            onDismiss = { viewModel.closeWriteReviewDialog() },
            onSubmit = { rating, title, comment, tags ->
                viewModel.submitReview(state.viewingProduct!!.id, rating, title, comment, tags)
            }
        )
    }
}
