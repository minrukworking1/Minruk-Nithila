package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Product

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WriteReviewDialog(
    product: Product,
    onDismiss: () -> Unit,
    onSubmit: (Int, String, String, List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableIntStateOf(5) }
    var reviewTitle by remember { mutableStateOf("") }
    var reviewComment by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf("Eco Packaging", "High Quality")) }

    val availableTags = listOf(
        "Eco Packaging",
        "High Quality",
        "Super Fresh",
        "Great Taste",
        "Fast EV Delivery",
        "Zero Waste",
        "Great Value"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag("write_review_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Write a Customer Review",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Star Rating Picker
                Text(
                    text = "Overall Rating: $rating ★",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                InteractiveStarRatingPicker(
                    currentRating = rating,
                    onRatingSelected = { rating = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Review Title Input
                OutlinedTextField(
                    value = reviewTitle,
                    onValueChange = { reviewTitle = it },
                    label = { Text("Title (e.g. Delicious & plastic-free)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed Review Input
                OutlinedTextField(
                    value = reviewComment,
                    onValueChange = { reviewComment = it },
                    label = { Text("Share your experience with this item") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tag Chips
                Text("Select tags:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                            },
                            label = { Text(tag, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // GreenPoints Reward notice
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "🌱 Publishing this review earns you +25 GreenPoints!",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        val finalTitle = if (reviewTitle.isBlank()) "Verified Experience" else reviewTitle
                        val finalComment = if (reviewComment.isBlank()) "Great eco-friendly product, highly recommended!" else reviewComment
                        onSubmit(rating, finalTitle, finalComment, selectedTags.toList())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_review_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Submit Verified Review", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
