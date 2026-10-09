package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ShoppingUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutSheet(
    state: ShoppingUiState,
    onDismiss: () -> Unit,
    onStepChange: (Int) -> Unit,
    onSelectAddress: (String) -> Unit,
    onSelectShipping: (String, Double) -> Unit,
    onSelectPayment: (String) -> Unit,
    onPlaceOrder: (Context) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val subtotal = state.cartItems.sumOf { it.product.price * it.quantity }
    val promoDiscount = subtotal * state.promoDiscountPercent
    val pointsDiscount = if (state.redeemPointsChecked) minOf(state.loyaltyProfile.points, (subtotal * 100).toInt()) / 100.0 else 0.0
    val total = maxOf(0.0, subtotal - promoDiscount - pointsDiscount + state.shippingCost)

    var cardNumberInput by remember { mutableStateOf("4242 •••• •••• 4242") }
    var cardExpiryInput by remember { mutableStateOf("12/28") }
    var cardCvvInput by remember { mutableStateOf("888") }
    var cardNameInput by remember { mutableStateOf("Maya Lin") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("checkout_modal_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secure",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "256-Bit Encrypted Checkout",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Step Indicator (1 to 4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val stepLabels = listOf("Address", "Delivery", "Payment", "Confirm")
                for (i in 1..4) {
                    val isActive = i <= state.checkoutStep
                    val isCurrent = i == state.checkoutStep
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { if (i < state.checkoutStep) onStepChange(i) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (i < state.checkoutStep) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Text(
                                    text = "$i",
                                    color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stepLabels[i - 1],
                            fontSize = 11.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (i < 4) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .padding(horizontal = 6.dp)
                                .background(if (i < state.checkoutStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Content
            when (state.checkoutStep) {
                1 -> {
                    // STEP 1: Address
                    Text("Shipping Address", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    val addresses = listOf(
                        "742 Evergreen Terrace, Eco District, CA 90210 (Home)",
                        "108 Green Valley Rd, Suite 400, San Francisco, CA 94107 (Work)",
                        "52 Elm Street, Apt 3B, Portland, OR 97201 (Alternate)"
                    )

                    addresses.forEach { addr ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectAddress(addr) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.selectedAddress == addr) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = state.selectedAddress == addr,
                                    onClick = { onSelectAddress(addr) },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(addr, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onStepChange(2) },
                        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("checkout_continue_to_step_2"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Continue to Delivery Method", fontWeight = FontWeight.Bold)
                    }
                }

                2 -> {
                    // STEP 2: Delivery Speed
                    Text("Eco Delivery Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    val shippingOptions = listOf(
                        Triple("Green Express (100% EV Delivery)", 0.0, "Delivered today by electric fleet - Zero carbon emissions"),
                        Triple("Same-Day Zero-Emission Courier", 4.99, "Priority electric cargo bike & van within 2 hours"),
                        Triple("Standard Carbon-Neutral Ground", 0.0, "Consolidated green dispatch within 2 days")
                    )

                    shippingOptions.forEach { (name, fee, desc) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectShipping(name, fee) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.selectedShippingMethod == name) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = state.selectedShippingMethod == name,
                                    onClick = { onSelectShipping(name, fee) },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.ElectricCar, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(if (fee == 0.0) "FREE" else String.format(Locale.US, "$%.2f", fee), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { onStepChange(1) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back")
                        }
                        Button(
                            onClick = { onStepChange(3) },
                            modifier = Modifier.weight(1.5f).height(50.dp).testTag("checkout_continue_to_step_3"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Select Payment", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                3 -> {
                    // STEP 3: Integrated Payment Gateways
                    Text("Secure Payment Gateway", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Select your preferred integrated checkout gateway:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    val paymentGateways = listOf(
                        Pair("Google Pay (Secure 1-Tap)", Icons.Default.Payments),
                        Pair("Credit or Debit Card", Icons.Default.CreditCard),
                        Pair("Apple Pay / UPI Direct", Icons.Default.AccountBalanceWallet),
                        Pair("Eco-Wallet & GreenPoints", Icons.Default.Shield),
                        Pair("Cash on Delivery (Eco-Verified)", Icons.Default.Money)
                    )

                    paymentGateways.forEach { (name, icon) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectPayment(name) }
                                .testTag("payment_method_${name.take(10)}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.selectedPaymentMethod == name) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = state.selectedPaymentMethod == name,
                                    onClick = { onSelectPayment(name) },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    // Card Fields if Card selected
                    AnimatedVisibility(visible = state.selectedPaymentMethod.contains("Credit")) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            OutlinedTextField(
                                value = cardNumberInput,
                                onValueChange = { cardNumberInput = it },
                                label = { Text("Card Number") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = cardExpiryInput,
                                    onValueChange = { cardExpiryInput = it },
                                    label = { Text("MM/YY") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = cardCvvInput,
                                    onValueChange = { cardCvvInput = it },
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = cardNameInput,
                                onValueChange = { cardNameInput = it },
                                label = { Text("Cardholder Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    // PCI Security Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "PCI-DSS Level 1 Encrypted • Zero card data stored on device",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { onStepChange(2) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back")
                        }
                        Button(
                            onClick = { onStepChange(4) },
                            modifier = Modifier.weight(1.5f).height(50.dp).testTag("checkout_continue_to_step_4"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Review Order", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                4 -> {
                    // STEP 4: Review & Place Order
                    Text("Review & Confirm Order", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("📍 Delivery: ${state.selectedAddress}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🚚 Method: ${state.selectedShippingMethod}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("💳 Payment: ${state.selectedPaymentMethod}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Items (${state.cartItems.sumOf { it.quantity }}):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            state.cartItems.forEach { item ->
                                Text("• ${item.quantity}x ${item.product.name} - \$${String.format(Locale.US, "%.2f", item.product.price * item.quantity)}", style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Final Total to Pay:", fontWeight = FontWeight.Bold)
                                Text(String.format(Locale.US, "$%.2f", total), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "🌱 You will earn ${(total * 10).toInt()} GreenPoints with this purchase!",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (state.isProcessingPayment) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Authorizing Secure Payment...", fontWeight = FontWeight.Bold)
                            Text("Contacting carbon-offset registry...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        Button(
                            onClick = { onPlaceOrder(context) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("place_secure_order_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Authorize & Place Order ($${String.format(Locale.US, "%.2f", total)})", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
