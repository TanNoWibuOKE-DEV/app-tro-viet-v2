package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.RoomProperty
import com.example.ui.MainViewModel
import com.example.ui.components.CommonHeader
import com.example.ui.components.NavTab
import com.example.ui.components.TroVietBottomBar
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AppointmentsScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TroVietApp()
            }
        }
    }
}

@Composable
fun TroVietApp(viewModel: MainViewModel = viewModel()) {
    val properties by viewModel.properties.collectAsState()
    val savedPropertyIds by viewModel.savedPropertyIds.collectAsState()
    val appointments by viewModel.appointments.collectAsState()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    var selectedProperty by remember { mutableStateOf<RoomProperty?>(null) }
    var isAiAssistantOpen by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle Android system back button
    BackHandler(enabled = selectedProperty != null || isAiAssistantOpen) {
        if (selectedProperty != null) {
            selectedProperty = null
        } else if (isAiAssistantOpen) {
            isAiAssistantOpen = false
        }
    }

    val showBottomBar = selectedProperty == null && !isAiAssistantOpen

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                TroVietBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it },
                    onAiAssistantClick = { isAiAssistantOpen = true },
                    showAiAssistantPill = currentTab == NavTab.HOME || currentTab == NavTab.MAP
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = when {
                    selectedProperty != null -> "detail"
                    isAiAssistantOpen -> "ai_assistant"
                    else -> currentTab.name
                },
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    "detail" -> {
                        selectedProperty?.let { prop ->
                            val isSaved = savedPropertyIds.contains(prop.id)
                            DetailScreen(
                                property = prop,
                                isSaved = isSaved,
                                onBack = { selectedProperty = null },
                                onSaveToggle = {
                                    viewModel.toggleSave(prop)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (!isSaved) "Đã lưu vào danh sách yêu thích" else "Đã bỏ lưu phòng"
                                        )
                                    }
                                },
                                onChatClick = {
                                    selectedProperty = null
                                    isAiAssistantOpen = true
                                },
                                onOpenMap = {
                                    selectedProperty = null
                                    currentTab = NavTab.MAP
                                },
                                onBookViewing = { date, time, phone, note ->
                                    viewModel.bookAppointment(prop, date, time, phone, note)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã đặt lịch hẹn xem phòng ngày $date lúc $time thành công!")
                                    }
                                }
                            )
                        }
                    }
                    "ai_assistant" -> {
                        AiAssistantScreen(
                            onBack = { isAiAssistantOpen = false },
                            onPropertyClick = { prop -> selectedProperty = prop },
                            onBookViewing = { prop, date, time, phone, note ->
                                viewModel.bookAppointment(prop, date, time, phone, note)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Đã đặt hẹn xem phòng ${prop.title} thành công!")
                                }
                            },
                            repository = viewModel.repository
                        )
                    }
                    NavTab.HOME.name -> {
                        androidx.compose.foundation.layout.Column {
                            CommonHeader(
                                title = "Trang Chủ",
                                subtitle = "Trang Chủ",
                                onNotificationClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Bạn có 2 gợi ý phòng mới quanh ĐH Sư Phạm")
                                    }
                                },
                                onProfileClick = { currentTab = NavTab.PROFILE }
                            )
                            HomeScreen(
                                properties = properties,
                                savedPropertyIds = savedPropertyIds,
                                onPropertyClick = { prop -> selectedProperty = prop },
                                onSaveToggle = { prop ->
                                    val isSaved = savedPropertyIds.contains(prop.id)
                                    viewModel.toggleSave(prop)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (!isSaved) "Đã lưu vào danh sách yêu thích" else "Đã bỏ lưu phòng"
                                        )
                                    }
                                },
                                onOpenMap = { currentTab = NavTab.MAP },
                                onOpenAiAssistant = { isAiAssistantOpen = true },
                                onBookViewing = { prop, date, time, phone, note ->
                                    viewModel.bookAppointment(prop, date, time, phone, note)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã đặt lịch hẹn xem phòng ngày $date thành công!")
                                    }
                                },
                                onOpenProfileSettings = { currentTab = NavTab.PROFILE }
                            )
                        }
                    }
                    NavTab.MAP.name -> {
                        androidx.compose.foundation.layout.Column {
                            CommonHeader(
                                title = "Bản Đồ",
                                subtitle = "Bản Đồ",
                                onNotificationClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã cập nhật radar tìm kiếm theo vị trí GPS")
                                    }
                                },
                                onProfileClick = { currentTab = NavTab.PROFILE }
                            )
                            MapScreen(
                                properties = properties,
                                savedPropertyIds = savedPropertyIds,
                                onPropertyClick = { prop -> selectedProperty = prop },
                                onSaveToggle = { prop ->
                                    val isSaved = savedPropertyIds.contains(prop.id)
                                    viewModel.toggleSave(prop)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (!isSaved) "Đã lưu vào danh sách yêu thích" else "Đã bỏ lưu phòng"
                                        )
                                    }
                                },
                                onBookViewing = { prop, date, time, phone, note ->
                                    viewModel.bookAppointment(prop, date, time, phone, note)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Đã đặt lịch xem phòng thành công!")
                                    }
                                }
                            )
                        }
                    }
                    NavTab.SAVED.name -> {
                        val savedList = properties.filter { savedPropertyIds.contains(it.id) }
                        SavedScreen(
                            savedProperties = savedList,
                            onPropertyClick = { prop -> selectedProperty = prop },
                            onRemoveSaved = { prop -> viewModel.toggleSave(prop) },
                            onBookViewing = { prop ->
                                selectedProperty = prop
                            },
                            onExploreRooms = { currentTab = NavTab.HOME }
                        )
                    }
                    NavTab.APPOINTMENTS.name -> {
                        AppointmentsScreen(
                            appointments = appointments,
                            onCancelAppointment = { id ->
                                viewModel.cancelAppointment(id)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Đã hủy lịch hẹn")
                                }
                            },
                            onExploreRooms = { currentTab = NavTab.HOME }
                        )
                    }
                    NavTab.PROFILE.name -> {
                        ProfileScreen(
                            onExploreRooms = { currentTab = NavTab.HOME }
                        )
                    }
                }
            }
        }
    }
}
