package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.model.OrderStatus
import com.example.ui.ShoppingUiState
import java.util.Locale

@Composable
fun OrderTrackingScreen(
    state: ShoppingUiState,
    onAdvanceStage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val order = state.activeOrder
    val language = state.selectedLanguage

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("order_tracking_screen"),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.get("order_tracking", language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Order ${order.orderNumber} • ${order.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "🌱 100% EV Delivery",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Live Simulated Map Route Canvas Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val lineColor = Color(0xFF2E7D32)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Draw background road lines
                        drawLine(
                            color = Color(0xFFC8E6C9),
                            start = Offset(40f, h * 0.7f),
                            end = Offset(w - 40f, h * 0.3f),
                            strokeWidth = 18f
                        )

                        // Draw active progress route
                        val progressFraction = when (order.status) {
                            OrderStatus.CONFIRMED -> 0.15f
                            OrderStatus.PACKED -> 0.35f
                            OrderStatus.IN_TRANSIT -> 0.65f
                            OrderStatus.OUT_FOR_DELIVERY -> 0.85f
                            OrderStatus.DELIVERED -> 1.0f
                        }

                        val startX = 40f
                        val startY = h * 0.7f
                        val endX = w - 40f
                        val endY = h * 0.3f

                        val currentX = startX + (endX - startX) * progressFraction
                        val currentY = startY + (endY - startY) * progressFraction

                        drawLine(
                            color = lineColor,
                            start = Offset(startX, startY),
                            end = Offset(currentX, currentY),
                            strokeWidth = 10f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
                        )

                        // Draw start hub dot
                        drawCircle(color = Color(0xFF1B5E20), radius = 12f, center = Offset(startX, startY))
                        // Draw destination house dot
                        drawCircle(color = Color(0xFF0D5338), radius = 14f, center = Offset(endX, endY))
                        // Draw moving courier dot
                        drawCircle(color = Color(0xFF43A047), radius = 18f, center = Offset(currentX, currentY))
                        drawCircle(color = Color.White, radius = 8f, center = Offset(currentX, currentY))
                    }

                    // Floating Driver Pill
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.92f),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ElectricCar, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Live EV Van #104",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }

                    // ETA Pill
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1B5E20),
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = "ETA: ${order.estimatedDeliveryTime}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Courier Contact Card & Simulation Action
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AR",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = order.courierName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = order.courierVehicle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { /* simulated call */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Courier",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Advance Stage Simulation Button
                    Button(
                        onClick = onAdvanceStage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("simulate_tracking_stage_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (order.status == OrderStatus.DELIVERED) "Restart Tracking Simulation" else "Simulate Next Live Step: ${order.status.label} ➡️",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Live Tracking Timeline Steps
        item {
            Text(
                text = "Tracking Timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(order.trackingSteps) { step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Circle Step Indicator & Vertical Line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    step.isCompleted -> MaterialTheme.colorScheme.primary
                                    step.isCurrent -> Color(0xFFFFB300)
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (step.isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(44.dp)
                            .background(
                                if (step.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Step Text
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = step.title,
                            fontWeight = if (step.isCurrent || step.isCompleted) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (step.isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = step.time,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Ordered Items Summary
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Items in this Package", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.product.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", item.product.price * item.quantity),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Paid (${order.paymentMethod})", fontWeight = FontWeight.Bold)
                        Text(String.format(Locale.US, "$%.2f", order.total), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
