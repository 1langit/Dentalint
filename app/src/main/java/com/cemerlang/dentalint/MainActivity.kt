package com.cemerlang.dentalint

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.data.model.checkup.PatientData
import com.cemerlang.dentalint.data.network.ApiClient
import com.cemerlang.dentalint.presentation.navigation.Dashboard
import com.cemerlang.dentalint.presentation.theme.DentalintTheme
import com.cemerlang.dentalint.presentation.ui.blog.BlogReadScreen
import com.cemerlang.dentalint.presentation.ui.capture.CaptureHistoryScreen
import com.cemerlang.dentalint.presentation.ui.capture.CaptureResultScreen
import com.cemerlang.dentalint.presentation.ui.chatbot.ChatbotScreen
import com.cemerlang.dentalint.presentation.ui.checkup.CheckupViewModel
import com.cemerlang.dentalint.presentation.ui.checkup.appointment.AppointmentRegisterScreen
import com.cemerlang.dentalint.presentation.ui.checkup.appointment.AppointmentRegisterSuccessScreen
import com.cemerlang.dentalint.presentation.ui.checkup.history.CheckupHistoryDetailScreen
import com.cemerlang.dentalint.presentation.ui.checkup.history.CheckupHistoryScreen
import com.cemerlang.dentalint.presentation.ui.checkup.history.CheckupHistoryViewModel
import com.cemerlang.dentalint.presentation.ui.checkup.patient.PatientRegisterScreen
import com.cemerlang.dentalint.presentation.ui.checkup.patient.PatientRegisterSuccessScreen
import com.cemerlang.dentalint.presentation.ui.login.LoginScreen
import com.cemerlang.dentalint.presentation.ui.notes.NotesAddScreen
import com.cemerlang.dentalint.presentation.ui.notes.NotesDetailScreen
import com.cemerlang.dentalint.presentation.ui.notification.NotificationScreen
import com.cemerlang.dentalint.presentation.ui.profile.ProfileEditScreen
import com.cemerlang.dentalint.presentation.ui.profile.ProfileScreen
import com.cemerlang.dentalint.presentation.ui.profile.ProfileViewModel
import com.cemerlang.dentalint.presentation.ui.register.RegisterScreen
import com.cemerlang.dentalint.presentation.ui.splash.DentalintSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        val prefManager = PrefManager(this)
        setContent {
            DentalintTheme(
//                darkTheme = when (prefManager.getString(PrefManager.Key.THEME)) {
//                    "Light" -> false
//                    "Dark" -> true
//                    else -> isSystemInDarkTheme()
//                }
            ) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        DentalintSplashScreen(
                            onNavigate = {
                                navController.navigate(it) {
                                    popUpTo("splash") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    navigation(startDestination = "login", route = "auth") {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                    }

                    navigation(startDestination = "dashboard", route = "main") {
                        composable("dashboard") {
                            Dashboard(navController, prefManager.getString(PrefManager.Key.NAME))
                        }

                        navigation(startDestination = "profile", route = "user") {
                            composable("profile") {
                                val viewModel = it.sharedViewModel<ProfileViewModel>(navController)
                                ProfileScreen(
                                    onBackClick = { navController.navigateUp() },
                                    onLogoutClick = {
                                        navController.popBackStack(
                                            navController.graph.startDestinationId,
                                            true
                                        )
                                        navController.navigate("login")
                                    },
                                    onEditClick = { navController.navigate("profile_edit") },
                                    viewModel = viewModel
                                )
                            }
                            composable("profile_edit") {
                                val viewModel = it.sharedViewModel<ProfileViewModel>(navController)
                                ProfileEditScreen(
                                    onBackClick = { navController.navigateUp() },
                                    viewModel = viewModel
                                )
                            }
                        }

                        composable("notification") {
                            NotificationScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        composable("chatbot") {
                            ChatbotScreen(navController)
                        }

                        composable("notes_add") {
                            NotesAddScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                        composable("notes_detail/{noteId}") {
                            val noteId = it.arguments?.getString("noteId")
                            NotesDetailScreen(
                                noteId = noteId,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        composable("capture_history") {
                            CaptureHistoryScreen(
                                onBackClick = { navController.navigateUp() },
                                onItemClick = { navController.navigate("capture_result/$it") }
                            )
                        }
                        composable("capture_result/{captureId}") {
                            val captureId = it.arguments?.getString("captureId")?.toInt()
                            CaptureResultScreen(
                                captureId = captureId,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        composable("blog_read/{blogId}") {
                            val blogId = it.arguments?.getString("blogId")
                            BlogReadScreen(
                                blogId = blogId,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        navigation(startDestination = "checkup_history_list", route = "checkup_history") {
                            composable("checkup_history_list") {
                                val viewModel = it.sharedViewModel<CheckupHistoryViewModel>(navController)
                                CheckupHistoryScreen(
                                    onItemClick = { navController.navigate("checkup_detail") },
                                    onBackClick = { navController.navigateUp() },
                                    viewModel = viewModel
                                )
                            }

                            composable("checkup_detail") {
                                val viewModel = it.sharedViewModel<CheckupHistoryViewModel>(navController)
                                val appointment = viewModel.getSelectedAppointment()
                                if (appointment != null) {
                                    CheckupHistoryDetailScreen(
                                        appointment = appointment,
                                        onBackClick = { navController.navigateUp() }
                                    )
                                } else {
                                    navController.navigateUp()
                                }
                            }
                        }

                        navigation(startDestination = "appointment_register/{clinicId}", route = "appointment/{clinicId}") {
                            composable("appointment_register/{clinicId}") {
                                val clinicId = it.arguments?.getString("clinicId")?.toInt()
                                val viewModel = it.sharedViewModel<CheckupViewModel>(navController)
                                LaunchedEffect(clinicId) {
                                    if (clinicId != null) {
                                        viewModel.selectClinic(clinicId)
                                    }
                                }
                                var appointment by remember { mutableStateOf<AppointmentData?>(null) }
                                if (appointment == null) {
                                    AppointmentRegisterScreen(
                                        onBackClick = { navController.navigateUp() },
                                        onRegisterAppointmentSuccess = { appointment = it },
                                        onRegisterPatientClick = { navController.navigate("patient_register") },
                                        viewModel = viewModel
                                    )
                                } else {
                                    AppointmentRegisterSuccessScreen(
                                        appointment = appointment!!,
                                        onBackClick = { navController.navigateUp() }
                                    )
                                }
                            }

                            composable("patient_register") {
                                val viewModel = it.sharedViewModel<CheckupViewModel>(navController)
                                var patient by remember { mutableStateOf<PatientData?>(null) }
                                if (patient == null) {
                                    PatientRegisterScreen(
                                        onBackClick = { navController.navigateUp() },
                                        onRegisterPatientSuccess = { patient = it },
                                        onRegisterAppointmentClick = { navController.navigateUp() },
                                        viewModel = viewModel
                                    )
                                } else {
                                    PatientRegisterSuccessScreen(
                                        patient = patient!!,
                                        onBackClick = { navController.navigateUp() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController) : T {
        val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
        val parentEntry = remember(key1 = this) {
            navController.getBackStackEntry(navGraphRoute)
        }
        return hiltViewModel(parentEntry)
    }
}