package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LooksTwo
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AiAnalysis
import com.example.data.model.ChatMessage
import com.example.data.model.RoomProperty
import com.example.data.model.SoftWeight
import com.example.data.repository.PropertyRepository
import com.example.ui.components.BookingDialog
import com.example.ui.components.CommonHeader
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietSecondary
import com.example.ui.theme.TroVietTertiary
import com.example.ui.theme.TroVietTertiaryContainer
import com.example.ui.theme.TroVietTertiaryFixed
import kotlinx.coroutines.launch

@Composable
fun AiAssistantScreen(
    onBack: () -> Unit,
    onPropertyClick: (RoomProperty) -> Unit,
    onBookViewing: (RoomProperty, String, String, String, String) -> Unit,
    repository: PropertyRepository
) {
    var messages by remember { mutableStateOf(repository.getInitialChatMessages()) }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    var feedbackStatus by remember { mutableStateOf<String?>(null) }
    var bookingPropertyTarget by remember { mutableStateOf<RoomProperty?>(null) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun handleSend(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            isUser = true,
            text = query,
            timestamp = "14:35"
        )
        messages = messages + userMsg
        inputText = ""
        isThinking = true

        scope.launch {
            listState.animateScrollToItem(messages.size - 1)
            kotlinx.coroutines.delay(1200)

            val (analysis, summary, recs) = when {
                query.contains("rẻ", ignoreCase = true) || query.contains("giá", ignoreCase = true) -> {
                    Triple(
                        AiAnalysis(
                            confidenceRate = 98,
                            maxBudget = "≤ 2.500.000 đ/tháng",
                            radiusKm = "≤ 2.5 km từ ĐH Ngoại Thương",
                            requiredAmenities = listOf("WC riêng", "Bình nóng lạnh", "Chỗ để xe"),
                            softWeightings = listOf(
                                SoftWeight("Tiết kiệm chi phí", 0.95f),
                                SoftWeight("Gần chợ sinh viên", 0.88f)
                            )
                        ),
                        "Đã tinh chỉnh ngân sách trần xuống 2.5 triệu. Dưới đây là các lựa chọn tiết kiệm nhất cho sinh viên:",
                        listOf(PropertyRepository.SAMPLE_PROPERTIES[2], PropertyRepository.SAMPLE_PROPERTIES[5])
                    )
                }
                query.contains("ban công", ignoreCase = true) -> {
                    Triple(
                        AiAnalysis(
                            confidenceRate = 97,
                            maxBudget = "≤ 3.200.000 đ/tháng",
                            radiusKm = "≤ 3.0 km",
                            requiredAmenities = listOf("Ban công thoáng", "Điều hòa", "WC khép kín"),
                            softWeightings = listOf(
                                SoftWeight("Ánh sáng tự nhiên", 0.96f),
                                SoftWeight("Không gian thoáng đãng", 0.92f)
                            )
                        ),
                        "Đã áp dụng bộ lọc nâng cao: Ban công hướng mát, đón ánh sáng tự nhiên cho việc học tập:",
                        listOf(PropertyRepository.SAMPLE_PROPERTIES[0], PropertyRepository.SAMPLE_PROPERTIES[5])
                    )
                }
                else -> {
                    Triple(
                        AiAnalysis(
                            confidenceRate = 95,
                            maxBudget = "≤ 3.200.000 đ/tháng",
                            radiusKm = "≤ 3.0 km",
                            requiredAmenities = listOf("Đầy đủ nội thất", "Điều hòa", "An ninh"),
                            softWeightings = listOf(
                                SoftWeight("Yên tĩnh tối ưu", 0.94f),
                                SoftWeight("Độ an toàn tài sản", 0.90f)
                            )
                        ),
                        "Hệ thống đã phân tích câu hỏi của bạn và đối chiếu thời gian di chuyển thực tế. Đây là đề xuất được tối ưu hóa:",
                        listOf(PropertyRepository.SAMPLE_PROPERTIES[0], PropertyRepository.SAMPLE_PROPERTIES[1])
                    )
                }
            }

            val aiMsg = ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                isUser = false,
                text = "Đã cập nhật theo yêu cầu mới của bạn.",
                timestamp = "14:35",
                structuredAnalysis = analysis,
                summaryText = summary,
                recommendedProperties = recs
            )

            messages = messages + aiMsg
            isThinking = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Common Header
        CommonHeader(
            title = "Trợ Lý Ai",
            subtitle = "Smart Rental AI",
            showBackButton = true,
            onBackClick = onBack,
            onShareClick = {}
        )

        // Sub-Bar: Smart Preference Engine
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(TroVietPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Smart Preference Engine",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Hệ thống phân tích nhu cầu • v2.4",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Live Assistant Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(TroVietTertiaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(TroVietTertiaryFixed)
                        )
                        Text(
                            text = "Live Assistant",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Messages Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Context timestamp
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Hôm nay, 14:32 • Phân tích ngữ cảnh sinh viên",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(messages) { msg ->
                if (msg.isUser) {
                    // User Message Bubble
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 19.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "Đã gửi", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                tint = TroVietTertiary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                } else {
                    // AI Response Stream
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // AI Assistant Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(TroVietPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = TroVietPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Trợ lý AI Trọ Việt",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Vừa phân tích",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Structured Extraction Card
                        msg.structuredAnalysis?.let { analysis ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Rule,
                                                contentDescription = null,
                                                tint = TroVietPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Bộ giải mã nhu cầu tự động",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "Độ tin cậy: ${analysis.confidenceRate}%",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TroVietTertiary
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Item 1: Budget
                                    FilterRow(
                                        icon = Icons.Default.Payments,
                                        label = "Ngân sách tối đa",
                                        value = analysis.maxBudget,
                                        tag = "Hard Filter",
                                        isPrimaryValue = true
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Item 2: Commute radius
                                    FilterRow(
                                        icon = Icons.Default.NearMe,
                                        label = "Bán kính di chuyển",
                                        value = analysis.radiusKm,
                                        tag = "Hard Filter",
                                        isPrimaryValue = false
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Item 3: Required amenities
                                    FilterRow(
                                        icon = Icons.Default.CheckCircle,
                                        label = "Tiện ích bắt buộc",
                                        value = analysis.requiredAmenities.joinToString(" • "),
                                        tag = "Khóa 100%",
                                        isPrimaryValue = false
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Item 4: Soft weighting
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = "Ưu tiên môi trường sống (Soft Weighting)",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                analysis.softWeightings.forEach { sw ->
                                                    Box(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(MaterialTheme.colorScheme.surface)
                                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                                    ) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(
                                                                    imageVector = if (sw.label.contains("Yên tĩnh")) Icons.Default.VolumeOff else Icons.Default.Shield,
                                                                    contentDescription = null,
                                                                    tint = TroVietTertiary,
                                                                    modifier = Modifier.size(14.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Text(
                                                                    text = sw.label,
                                                                    fontSize = 11.sp,
                                                                    color = MaterialTheme.colorScheme.onSurface,
                                                                    maxLines = 1
                                                                )
                                                            }
                                                            Text(
                                                                text = "${sw.weight}/1.0",
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = TroVietPrimary
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Synthesis text
                        msg.summaryText?.let { summary ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = summary,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }

                        // Recommendation cards in chat stream
                        msg.recommendedProperties?.forEachIndexed { index, prop ->
                            AiPropertyCard(
                                property = prop,
                                rank = index + 1,
                                score = if (index == 0) 95 else 92,
                                onCardClick = { onPropertyClick(prop) },
                                onBookClick = { bookingPropertyTarget = prop }
                            )
                        }

                        // Clarification & Feedback Loop Widget (Only on latest AI message)
                        if (msg == messages.lastOrNull { !it.isUser }) {
                            FeedbackWidget(
                                feedbackStatus = feedbackStatus,
                                onFeedback = { feedbackStatus = it },
                                onQuickPromptClick = { prompt -> handleSend(prompt) }
                            )
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(TroVietPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "Đang kích hoạt thuật toán xếp hạng và quét phòng...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Bottom Chat Input Dock
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attach button
                IconButton(
                    onClick = { handleSend("Định vị phòng xung quanh bán kính 2km") },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLocationAlt,
                        contentDescription = "Vị trí",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text Input
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Hỏi hoặc mô tả thêm nhu cầu phòng...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { handleSend("Lọc phòng có máy giặt và tủ lạnh") }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("ai_chat_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TroVietPrimary)
                        .clickable { handleSend(inputText) }
                        .testTag("btn_send_message"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Gửi",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    bookingPropertyTarget?.let { prop ->
        BookingDialog(
            property = prop,
            onDismiss = { bookingPropertyTarget = null },
            onConfirm = { date, time, phone, note ->
                onBookViewing(prop, date, time, phone, note)
                bookingPropertyTarget = null
            }
        )
    }
}

@Composable
private fun FilterRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    tag: String,
    isPrimaryValue: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPrimaryValue) TroVietPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(TroVietTertiary.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(12.dp))
                Text(
                    text = tag,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TroVietTertiary
                )
            }
        }
    }
}

@Composable
private fun AiPropertyCard(
    property: RoomProperty,
    rank: Int,
    score: Int,
    onCardClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("ai_match_card_$rank")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Rank and Score Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (rank == 1) TroVietPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (rank == 1) Icons.Default.Verified else Icons.Default.LooksTwo,
                            contentDescription = null,
                            tint = if (rank == 1) TroVietPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Rank #$rank Match",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (rank == 1) TroVietPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(TroVietTertiary.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Grade,
                            contentDescription = null,
                            tint = TroVietTertiary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Score: $score/100",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TroVietTertiary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Photo Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = property.imageUrl,
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Bottom badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (rank == 1) Icons.Default.DirectionsWalk else Icons.Default.NearMe,
                            contentDescription = null,
                            tint = if (rank == 1) TroVietTertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = property.distanceInfo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Room Code
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Mã: TRV-88$rank",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing & title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = property.formattedPrice.replace("/tháng", ""),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TroVietPrimary
                    )
                    Text(text = "/tháng", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    text = "${property.area} • ${property.floor}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = property.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PinDrop,
                    contentDescription = null,
                    tint = TroVietPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = property.address,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reason Quote
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = TroVietPrimary,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = property.aiReasons.firstOrNull() ?: "Phù hợp ngân sách và cự ly di chuyển.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                property.amenities.take(3).forEach { am ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = am, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCardClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Xem 3D Tour", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onBookClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TroVietPrimary)
                ) {
                    Icon(Icons.Default.EventAvailable, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đặt lịch xem", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun FeedbackWidget(
    feedbackStatus: String?,
    onFeedback: (String) -> Unit,
    onQuickPromptClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Đề xuất này có đúng nhu cầu của bạn không?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Phản hồi giúp Trợ lý AI tinh chỉnh trọng số tìm kiếm chính xác hơn cho lần lọc tiếp theo.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isUp = feedbackStatus == "up"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isUp) TroVietTertiaryFixed else MaterialTheme.colorScheme.surface)
                        .clickable { onFeedback("up") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null,
                            tint = if (isUp) TroVietTertiary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rất phù hợp",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isUp) TroVietTertiary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                val isDown = feedbackStatus == "down"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDown) TroVietPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface)
                        .clickable { onFeedback("down") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Tune,
                            contentDescription = null,
                            tint = if (isDown) TroVietPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chưa ưng ý",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDown) TroVietPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "GỢI Ý CÂU HỎI NHANH:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            val quickChips = listOf(
                "Tìm phòng rẻ hơn chút nữa",
                "Muốn phòng có ban công",
                "So sánh 2 phòng này"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickChips.forEach { qc ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onQuickPromptClick(qc) }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (qc.contains("So sánh")) Icons.AutoMirrored.Filled.CompareArrows else Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = TroVietPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = qc,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
