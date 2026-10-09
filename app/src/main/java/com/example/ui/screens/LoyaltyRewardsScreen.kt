package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.model.LoyaltyTier
import com.example.ui.ShoppingUiState

@Composable
fun LoyaltyRewardsScreen(
    state: ShoppingUiState,
    onRedeemVoucher: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = state.loyaltyProfile
    val language = state.selectedLanguage

    val progressToNext = (profile.points.toFloat() / profile.nextTierPoints.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("loyalty_rewards_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Header
        item {
            Text(
                text = AppStrings.get("loyalty_rewards", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Earn points with every sustainable purchase & climate action",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // GreenPoints Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF0F5132), Color(0xFF198754))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Eco, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = profile.currentTier.tierName,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            Text(
                                text = profile.currentTier.multiplier,
                                color = Color(0xFFD1E7DD),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${profile.points}",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Active GreenPoints = \$${String.format("%.2f", profile.points / 100.0)} discount value",
                            color = Color(0xFFD1E7DD),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress to next tier
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tier Progress", color = Color.White, style = MaterialTheme.typography.labelSmall)
                                Text("${profile.points} / ${profile.nextTierPoints} pts", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progressToNext },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF5CD68D),
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tree Planting Impact Widget
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forest,
                            contentDescription = null,
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${profile.treesPlanted} Trees Planted in Your Name",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Every \$40 spent funds reforestation projects in certified biodiversity reserves.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // How to Earn Points Guide
        item {
            Text("How to Earn Points", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val earnRules = listOf(
                Pair("Shop Organic Goods", "Earn 10 GreenPoints per \$1 spent on any item"),
                Pair("Write Verified Review", "Earn 25 GreenPoints for sharing customer feedback"),
                Pair("Select EV Delivery", "Earn 15 bonus points for electric van shipping"),
                Pair("Return Reusable Packaging", "Earn 50 GreenPoints per carton returned for circular reuse")
            )

            earnRules.forEach { (title, desc) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Redeemable Rewards Vouchers
        item {
            Text("Redeem Rewards & Perks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val vouchers = listOf(
                Triple("\$5.00 Cart Discount", 500, "Direct coupon applied instantly to your cart subtotal"),
                Triple("Free Same-Day EV Courier", 250, "Zero-emission rush shipping on your next order"),
                Triple("Plant a Mangrove Tree", 300, "Official tree planted with certificate & GPS coordinates"),
                Triple("GreenCart Organic Tote Bag", 400, "100% fair-trade upcycled cotton tote shipped free")
            )

            vouchers.forEach { (title, cost, desc) ->
                val canRedeem = profile.points >= cost
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(if (canRedeem) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = if (canRedeem) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("$cost Points", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onRedeemVoucher(title, cost) },
                            enabled = canRedeem,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(if (canRedeem) "Redeem" else "Need $cost", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
