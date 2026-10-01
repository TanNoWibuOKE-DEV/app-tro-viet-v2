package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietSurfaceContainerHigh

const val AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuCYpD_dR0LklB2coqesPODxOKPX7LxulbbFk_S-Ni8U4E9ueA2K5rpK7pvpYKBogK2e4n8A2JxNf3q2DkkaCPzhndMPeDigMogGZ4saiEL7nwsvNFZz4DqDSlVU4TlBFm9sKynbp_GSt5hlt1kaGLDCF1ebWi4mm5iTgsA_LUuZFJm7fg6O4QqzKceB2LPAaGBo-mSDfFlaZVRjN1amiS1Ss6bUXANMuVOeH_oSDC-C7mRD-33V7TGO"
const val LOGO_URL = "https://lh3.googleusercontent.com/aida/AEtjO1UnGU9h2UvnzuvOxGcI769gZfa9oG7jdjqykEkgFzRMITLaqxhY8UeynZKiUnZP8snUJJOBSLRL65qPu_d5resFhS0JSURMyy2QZN-MHq9pxyIs_OKuDusk6iqNTyA8fycEk8e78OgAOq7oJMoYgiecVH9MQFatLnxeGwfVsWs49jR_9P7jtFMpFpoYh29-D_CLmG5dwLb3G4YuBP3ZIuooXT0OjwjsLTIM5H2xvzdjO0DP4APV8ZoSgA"

@Composable
fun CommonHeader(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onShareClick: (() -> Unit)? = null,
    onProfileClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Logo & Title block
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // House Icon badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TroVietPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Trọ Việt Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TRỌ ",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "VIỆT",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = TroVietPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    Text(
                        text = (subtitle ?: title).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right actions
            if (onShareClick != null) {
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Chia sẻ",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Box {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TroVietPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // User Profile Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape)
                    .clickable { onProfileClick() }
            ) {
                AsyncImage(
                    model = AVATAR_URL,
                    contentDescription = "Hồ sơ người dùng",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
