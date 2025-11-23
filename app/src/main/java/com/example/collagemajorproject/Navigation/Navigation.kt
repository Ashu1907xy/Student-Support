package com.example.collagemajorproject.Navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.collagemajorproject.AuthScreen.LoginPage
import com.example.collagemajorproject.AuthScreen.SignUpPage
import com.example.collagemajorproject.DataModel.Notes
import com.example.collagemajorproject.Screens.AboutScreen
import com.example.collagemajorproject.Screens.CollageNotesPaper.TabNotesPaperScreen
import com.example.collagemajorproject.Screens.Notes.AddNoteScreen
import com.example.collagemajorproject.Screens.Notes.DisplayNoteScreen
import com.example.collagemajorproject.Screens.DrawerScreen
import com.example.collagemajorproject.Screens.FacultySupport.FacultyScreen
import com.example.collagemajorproject.Screens.Notes.EditNoteScreen
import com.example.collagemajorproject.Screens.Profile.EditProfileScreen
import com.example.collagemajorproject.Screens.HomeScreen
import com.example.collagemajorproject.Screens.ItemData.ItemDataScreen
import com.example.collagemajorproject.Screens.PickMe.AddLostItemScreen
import com.example.collagemajorproject.Screens.PickMe.PickMeScreen
import com.example.collagemajorproject.Screens.Profile.ProfileScreen
import com.example.collagemajorproject.Screens.SplashScreen
import com.example.collagemajorproject.Screens.TimeTable.tabsScreen.TabScreen
import com.example.collagemajorproject.Screens.TimeTable.tabsScreen.ViewImageScreen
import com.example.collagemajorproject.Screens.TimeTable.tabsScreen.ViewPdfScreen
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel
import com.example.collagemajorproject.ViewModel.TimetableViewModel.TimeTableViewModel


@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    viewModel: TimeTableViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()



    NavHost(navController = navController, startDestination = "splash", builder = {

        composable("splash") {
            SplashScreen(modifier, navController, authViewModel)
        }

        composable("about") {
            AboutScreen(navController = navController)
        }

        composable("signup") {
            SignUpPage(
                modifier = Modifier,
                navController,
                authViewModel = authViewModel
            )
        }

        composable("Store") {
            ItemDataScreen(navController = navController)
        }
        composable("login") {
            LoginPage(
                modifier = Modifier,
                navController,
                authViewModel = authViewModel
            )
        }


        composable("home") {

            HomeScreen(modifier = Modifier, navController)
        }

        composable("drawer") {
            DrawerScreen(
                modifier = Modifier,
                navController,
                authViewModel = authViewModel
            )
        }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("Editprofile") { EditProfileScreen(authViewModel = authViewModel) }


        // Notes

        composable("Notes") {
            DisplayNoteScreen(navController)
        }

        composable("Add") {
            AddNoteScreen(navController)
        }

        composable(
            "edit/{id}/{quote}/{book}/{author}/{page}", arguments = listOf(

                navArgument("id") { type = NavType.StringType },
                navArgument("quote") { type = NavType.StringType },
                navArgument("book") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("page") { type = NavType.StringType }

            )) { backStackEntry ->
            val args = backStackEntry.arguments!!

            EditNoteScreen(
                navController, Notes(
                    id = args.getString("id") ?: " ",
                    quote = args.getString("quote") ?: " ",
                    book = args.getString("book") ?: " ",
                    author = args.getString("author") ?: " ",
                    page = args.getString("page") ?: " ",

                    )
            )
        }

        composable("Faculty") {
            FacultyScreen(navController = navController)
        }


        composable(
            route = "img_screen/{timetableImage}/{year}/{branch}"
        ) { backStackEntry ->

            val timetableImage =
                Uri.decode(backStackEntry.arguments?.getString("timetableImage") ?: "")
            val year = backStackEntry.arguments?.getString("year") ?: ""
            val branch = backStackEntry.arguments?.getString("branch") ?: ""

            ViewImageScreen(
                navController = navController,
                timetableImage = timetableImage,
                year = year,
                branch = branch

            )
        }


        composable(route = "pdf_screen/{pdf}/{subjectName}/{subjectCode}")
        { backStackEntry ->
            val pdf = Uri.decode(backStackEntry.arguments?.getString("pdf")) ?: ""
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
            val subjectCode = backStackEntry.arguments?.getString("subjectCode") ?: ""

            ViewPdfScreen(
                navController = navController,
                pdf = pdf,
                subjectCode = subjectCode,
                subjectName = subjectName
            )
        }


        composable("Timetable") {
            TabScreen(navController)
        }
        composable("Material") {
            TabNotesPaperScreen(navController)
        }



        composable ("Pickme"){
            PickMeScreen(navController = navController)
        }
        composable ("Addpick"){
            AddLostItemScreen(navController = navController)
        }


    })


}
