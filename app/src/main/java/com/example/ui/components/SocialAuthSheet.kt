package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialAuthSheet(
    onDismiss: () -> Unit,
    onSelectProvider: (AuthProvider) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("social_auth_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Quick & Secure Access",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Connect with social login or email for synced cart & points",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Google Login Button
            AuthOptionButton(
                title = "Continue with Google",
                iconText = "G",
                iconColor = Color(0xFF4285F4),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onSelectProvider(AuthProvider.GOOGLE) },
                testTag = "login_google_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Apple Login Button
            AuthOptionButton(
                title = "Continue with Apple",
                iconText = "",
                iconColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onSelectProvider(AuthProvider.APPLE) },
                testTag = "login_apple_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone SMS Login Button
            AuthOptionButton(
                title = "Continue with Phone SMS OTP",
                iconVector = Icons.Default.Phone,
                iconColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onSelectProvider(AuthProvider.PHONE) },
                testTag = "login_phone_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Email Login Button
            AuthOptionButton(
                title = "Continue with Email & Password",
                iconVector = Icons.Default.Email,
                iconColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onSelectProvider(AuthProvider.EMAIL) },
                testTag = "login_email_button"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Guest Option
            OutlinedButton(
                onClick = { onSelectProvider(AuthProvider.GUEST) },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("login_guest_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue as Eco Guest", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security note
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8F5E9)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF1B5E20), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Encrypted credentials with automated privacy compliance.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AuthOptionButton(
    title: String,
    iconText: String? = null,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconColor: Color = Color.Black,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconText != null) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        fontWeight = FontWeight.ExtraBold,
                        color = iconColor,
                        fontSize = 16.sp
                    )
                }
            } else if (iconVector != null) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
