package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.localization.AppStrings
import com.example.model.AppLanguage
import com.example.ui.AppScreen

data class NavItem(
    val screen: AppScreen,
    val titleKey: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val navItems = listOf(
    NavItem(AppScreen.HOME, "app_name", Icons.Default.Home, Icons.Outlined.Home, "nav_home"),
    NavItem(AppScreen.SEARCH, "categories", Icons.Default.Search, Icons.Outlined.Search, "nav_search"),
    NavItem(AppScreen.WISHLIST, "wishlist", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder, "nav_wishlist"),
    NavItem(AppScreen.CART, "cart", Icons.Default.ShoppingBag, Icons.Outlined.ShoppingBag, "nav_cart"),
    NavItem(AppScreen.ORDER_TRACKING, "order_tracking", Icons.Default.LocalShipping, Icons.Outlined.LocalShipping, "nav_tracking"),
    NavItem(AppScreen.PROFILE, "profile", Icons.Default.Person, Icons.Outlined.Person, "nav_profile")
)

@Composable
fun GreenCartBottomNav(
    currentScreen: AppScreen,
    cartCount: Int,
    wishlistCount: Int,
    language: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        navItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            val title = when (item.screen) {
                AppScreen.HOME -> "Home"
                AppScreen.SEARCH -> "Explore"
                AppScreen.WISHLIST -> "Wishlist"
                AppScreen.CART -> "Cart"
                AppScreen.ORDER_TRACKING -> "Track"
                AppScreen.PROFILE -> "Account"
                else -> AppStrings.get(item.titleKey, language)
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    when (item.screen) {
                        AppScreen.CART -> {
                            BadgedBox(
                                badge = {
                                    if (cartCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text("$cartCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = title,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        AppScreen.WISHLIST -> {
                            BadgedBox(
                                badge = {
                                    if (wishlistCount > 0) {
                                        Badge(
                                            containerColor = Color(0xFFE53935),
                                            contentColor = Color.White
                                        ) {
                                            Text("$wishlistCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = title,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        else -> {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = title,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                label = { Text(text = title, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

@Composable
fun GreenCartNavigationRail(
    currentScreen: AppScreen,
    cartCount: Int,
    wishlistCount: Int,
    language: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.testTag("nav_rail"),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {
            // Optional header logo
        }
    ) {
        navItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            val title = when (item.screen) {
                AppScreen.HOME -> "Home"
                AppScreen.SEARCH -> "Explore"
                AppScreen.WISHLIST -> "Wishlist"
                AppScreen.CART -> "Cart"
                AppScreen.ORDER_TRACKING -> "Track"
                AppScreen.PROFILE -> "Account"
                else -> AppStrings.get(item.titleKey, language)
            }

            NavigationRailItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen == AppScreen.CART && cartCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text("$cartCount")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = title
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = title
                        )
                    }
                },
                label = { Text(text = title) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("${item.testTag}_rail")
            )
        }
    }
}
