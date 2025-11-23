package com.example.collagemajorproject.Screens.FacultySupport

import android.R.attr.icon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collagemajorproject.Screens.Activity
import kotlinx.coroutines.delay












import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyScreen(navController: NavController) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        delay(1500)
        isLoading = false
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                            start = Offset(0f, 0f),
                            end = Offset(1500f, 0f)
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Faculty Directory",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "SIRT Bhopal",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Search by name, email or phone",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            label = "All Faculty",
                            selected = selectedFilter == "All",
                            onClick = { selectedFilter = "All" }
                        )
                        FilterChip(
                            label = "HOD/HOI",
                            selected = selectedFilter == "HOD/HOI",
                            onClick = { selectedFilter = "HOD/HOI" }
                        )
                        FilterChip(
                            label = "Teaching Staff",
                            selected = selectedFilter == "Faculty / Teaching Staff",
                            onClick = { selectedFilter = "Faculty / Teaching Staff" }
                        )
                    }
                }
            }

            if (isLoading) {
                EnhancedLoadingIndicator()
            } else {
                FacultyContactCards(
                    context = context,
                    searchQuery = searchQuery,
                    filterType = selectedFilter
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.height(36.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EnhancedLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp
                )
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Loading Faculty Directory...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Please wait",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun FacultyContactCards(
    context: Context,
    searchQuery: String,
    filterType: String
) {
    val allFaculty = remember {
        listOf(
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MANOJ SHARMA", "9826414177", "sirtmns@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Kirti Kumar Jain", "9074234336", "kjain1969@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Mona P Patel", "9174449343", "monapatel056@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PREETI SHARMA", "9752049465", "preetisharmabpl1979@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Dr Ritu Tiwari", "8871727100", "deepuritutiwari@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAJNISH KUMAR DWIVEDI", "7723008999", "rkdmaths@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "FEEROZ MANSOOR", "9109078266", "feerozmansoor@yahoo.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "JAYANT DESHMUKH", "8109156279", "jkdeshmukh@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Gaurav Sharma", "9827782103", "gouravsirt11@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHALINI SAHAY", "9926547456", "shalinisahay2020@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VISHWANATH T TIWARI", "9893614538", "tiwari.vishwanath@rediffmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Prachi Wadhawan", "7999382107", "prachibnd22@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MOHIT SINGH TOMAR", "9977867664", "mohitishere7@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NAVNEET KAUR", "9826258544", "navec2000@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "GHANSHYAM JAWALKAR", "7415366884", "kkrishnajawalkar@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VIJAY BHANDARI", "8770186481", "vijaysirt@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RABIYA HANFI", "7999969431", "Rabiya.hanfi99@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Sarla Raigar Raigar", "9098676093", "sarla.raigar@yahoo.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAJENDRA MUHARE", "8602296370", "rajmuhare@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ARUN KUMAR JHAPATE", "9754914446", "arunjhapate11@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANSHU SHRIVASTAVA", "9907857435", "shrivastava.anshu@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEENANSHA JAIN", "9406582186", "neenanshajain2011@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ASHUTOSH PANDEY", "9425605228", "ashutosh1228ster@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SOURABH BIROLE", "9993585658", "sbsbirole@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DINESH KUMAR KOLI", "9425679837", "dkoli157@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RUCHI DRONAWAT", "7771011091", "Dron.ruchi@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RUPALI CHAURE", "9425674409", "rupali.chaure@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ASHISH CHOUREY", "9893521043", "erashishchourey@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AMIT SHRIVASTAVA", "7024140521", "sagar.amitshri@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "TRIVENI SINGH", "7389484704", "triwenisingh01@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHREYA DESHMUKH", "8461856091", "shreyadeshmukh4444@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AMRITA TIWARI", "9977127286", "amritashroti@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANUPAMA JAIN", "9425150264", "Jain.anupama.vds@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRASANN JAIN", "9770000965", "prasannansh09@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "CHETNA SINGH", "9826330541", "chetnasinghbhopal@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR.SHIVANGI DWIVEDI", "9755218265", "shiva.dwivedi@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VIDOSH MAHATE", "7999951822", "Vidoshmahate@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "BHOOPENDRA SINGH RAJPUT", "8718897300", "Bhoopendrarajpoot30@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "INDRAKANT SINGH", "9926853790", "indrakant87@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR.SANTOSH SANODIYA", "9826927301", "santoshsanodiya09@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR.ALOK AGRAWAL", "9827497544", "alokag03@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RUPAL SHUKLA", "9981229462", "rupalshukla456@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MUKESH YADAV", "9303827413", "mukeshsinghyadav@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRIYANKA SONI", "7869543482", "Priyanka.soni@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRAKASH KATDARE", "7769868112", "katdarep21@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MR . SHIVRAJ SINGH", "7697954888", "86.shivraj@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. BRAJESH MOHAN GUPTA", "9407281100", "brajesh.mg@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MR. JEETENDRA MISHRA", "8319132855", "jeetendra.mishra10@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SUCHITRA SRIVASTAVA", "7042514860", "suchitra.srivastava@yahoo.co.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ARVIND SINGH GAUR", "9005475322", "eng.arvindgaur@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR.SEEMA RAFIQUE", "8889415796", "Seemarafique@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEHA CHAWRE", "8770772945", "nehachawre0402@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Babita Saxena Saxena", "9617490370", "babitasaxena32@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRACHI SHARMA", "9977917980", "Prachisirt1@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANJALI VISHWAKRMA", "7999849034", "vishvakarmaanjali4@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SONAM DUBEY", "7000277238", "sonamdubeyky@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PREETI DIXIT", "8770971280", "Preetidixit.sirt@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MONIKA KHERAJANI", "7470877899", "Monika.kherajni@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "TASNEEM JAHAN", "8989191400", "tasneem.cse@sirt.bhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRAVEEN KUMAR KAITHAL", "9039238899", "Praveenkaithal87@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SUCHITA SHARMA", "9926979948", "Vijusuchi@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SOURABH SINGH", "7869424422", "sourabh.aiml@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MAHENDRA JOSHI", "9406528127", "mailmjoshi@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANSHUL JAIN", "8109604985", "jainanshul17@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANKITA AWASTHI", "9424696199", "ankitaawasthi2010@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Yogesh Sahu", "9584826490", "yogesh.sahu22@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEEPAK MALVIYA", "9893896014", "deepakmalviya014@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ATUL KUMAR VAIDYA", "9589312927", "atulkumarvaidya@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAKESH KUMAR SHRIVASTAVA", "8120487528", "shrirakesh11@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRAMOD KUMAR", "7987877437", "PramodKumar.ec@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHWETA BILEY", "8959483325", "dabreshweta25@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "JISHA JAGADEVAN", "7828508196", "jishadev1995@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MADHAV SAHU", "8818953989", "Madhav.s@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DIVYA KHADE", "9131379257", "divyakhade1407@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. ANUKRITI SHARMA", "7746077745", "sharma.anukriti15@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VIVEK RAWAT", "9584475174", "vivek.rawat7075@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. NIDHI SINGH", "9425149770", "nidhi.it@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "BARKHA SHARMA", "9074847439", "barkha.sharma@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Neha Khare", "7067319979", "nkhare429@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "CHHOTELAL KUSHWAHA", "9340734596", "kushwaha.chhotelal148@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "POONAM CHOUBEY", "9302827092", "Poonam.it@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "KHUSHBOO VERMA", "9301380563", "sai.khushbu20@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MEHAJABEEN FATIMA", "9131526658", "drfatimaec@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AARTI VISHAL DOLI", "9165430166", "koranneaarti@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "HARITIMA MISHRA", "7987854990", "haritima.m@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEEPTI KHARE", "8770878115", "drdeepti.asc@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. SUSMITA SAHA", "9981378830", "pritha024@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PANKAJ BADGAIYAN", "8839536472", "pankajbadgaiyan86@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AAKRATI VERMA", "7999515695", "aakrativerma206@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Prashant Garg", "9630379383", "prashantvits17@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEVENDRA TIWARI", "9407263970", "devendra4483@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ARTI PRASAD", "9406511799", "prasadarti22@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHAILENDRA SINGH", "9589442468", "ssrajpoot1989@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Dr Rachna P Prasad", "9926560119", "rachnaprasaddr@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANSHITA PATIL", "8878372261", "anshitapatil0@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DHEERAJ JAIN", "9039202448", "djjain2448@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "UMESH KUMAR GERA", "7470484010", "sirtumesh@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VINEET KUMAR GUPTA", "7000401228", "vineet.kgupta@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "POOJA SINGH", "9424801029", "Poojasingh.sirts@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. GURUSHARAN KAUR", "8319645873", "kdrgurusharan@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AMIT SINGH SENGAR", "7506310404", "amitsengar.sirt@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "HARSHA TAVSE", "9977028193", "tavseharsha24@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHUBHAM GANGRADE", "9425889767", "shubham.cse@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "CHANDRA SHEKHAR DHAMANDE", "9229425368", "chandrashekhardhamande@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEEPSHIKHA ACHARYA", "7898458086", "deepshikha.ha@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEEPAK GWALE", "8965943822", "deepakgwale@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SRAJANA MALVIYA", "7000424725", "srajanamalviya1@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MADHURI SINGH", "7389802252", "s.r.madhuriphy@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "CHETAN GUPTA", "9589412366", "chetangupta.gupta1@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NIDHI GOUR", "9179510254", "gournidhi31@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAKESH KUMAR VERMA", "9009028146", "rakeshvrm54@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SURAJ KUMAR SINGH TOMAR", "8770512912", "surajsirt16@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SURENDRA BADGUJAR", "8871946103", "surendrabadgujar7231@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MEENA MOURYA", "9826943357", "meenakuhu@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DIGANT ARORA", "9826088248", "digantarora80@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DEEPSHIKHA KEDARE", "8962671347", "deepshikha.k@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEELU SINGH", "9425008914", "drneelusingh1973@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEERAJ KATIYAR", "7987236932", "neerajkatiyar7005@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. GAURAV SAHU", "9893869105", "gauravsahupoetry@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SUHAVANA DUBEY", "9406903263", "suhavnacommunication507@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AJIT PRAKASH RASTOGI", "9303479304", "bitsmech2009@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VANDANA RAI", "9174056405", "vandana.rai2113@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEHA SHARMA", "8305026023", "nehasharma.102125.cse@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RICHA SINGH", "7879549916", "richa030290singh@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "JAYRAM DWIVEDI", "7987867563", "jairam.it@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "LEESA SANTOSH", "9425665462", "sherilleesa7@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. PRADEEP DWIVEDI", "9993960790", "pkdwivedi76@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MONIKA JHAPATE", "9174806226", "monikajhapate24@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SHIVENDRA SINGH", "8109690317", "shivendra10sep90@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "IZHAR MOHD KHAN", "9977867847", "izhar_truba@yahoo.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "TANMAY AWASTHI", "9229229291", "tanmay2944@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PANKAJ DUBEY", "9981770382", "pankaj.ec@sirtbhopal.ac.in"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SACHIN JAT", "9977799032", "Jatsachin41@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PRIYA SHARMA", "8889387434", "priya.revisited19@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RESHMA JAIN", "9754633910", "Komal.reshmajain@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAJESH SAHU", "9893840270", "rajeshsahu50@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SARITA SAHNI", "7692812738", "saritasahni735@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DR. ARUNA SENGAR", "9893846699", "sengar.aruna1@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "SAPNA RAIKWAR", "9993951328", "sapnamanjhi2@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "HARSHITA SHUKLA", "9770410060", "harshitashukla@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AMIT VISHWAKARMA", "9340917829", "amitvishwa4@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "MOHAN SINGH", "9111020472", "msingh.mmmdp@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VANDANA DUBEY", "9450839603", "sirtdrvandanadubey@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "VIDHI SAHU", "8462997605", "vidhisahuvidhi27@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "POOJA LALWANI", "6265325214", "poo.lalwani06@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NEHA CHATURVEDI", "8928376710", "chaturvedi.nc@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "RAM MANOHAR KUSHWAHA", "6260305668", "rkushwaha0@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "ANISH ARYA", "9826706752", "anisharya@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "DIVYA PRAKASH WADHWANI", "7722872121", "Divyawadhwani12@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "AMIT KUMAR RATHORE", "8109261926", "amitrathore0411@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "EKTA DUBEY", "9340456284", "ektadubey829@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "NIRJHAR GUPTA", "8962616261", "nirjhargupta070@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "PUSHPENDRA SINGH PALASH", "8223922206", "palash.rgpv@gmail.com"),
            FacultyContactsData(Icons.Default.Person,"Faculty / Teaching Staff", "Diksha Pawar", "8719925063", "divya.rgpv@gmail.com"),


// HOD/HOI Contacts Data - Sagar Institute of Research and Technology, Bhopal

            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "AUMREESH KUMAR SAXENA", "9826399958", "aumreesh@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "JYOTI JAIN", "8989958062", "hodec@sirtbhopal.ac.in"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "SWATI PANDEY", "7869995603", "swatisharma.1987@yahoo.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "HRIDAYESH VERMA", "9926511192", "hsvarma79@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "KAPIL CHATURVEDI", "9907004447", "Kapil.rgtu@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "N K SAGAR", "9981662929", "nksagarme@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "KALPANA RAI", "7566598524", "kalpna.rai123@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "RITU SHRIVASTAVA", "9685445747", "ritushrivastava08@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "UDAY PANWAR", "9826752267", "panwaruday1@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "DHARMENDRA TYAGI", "7879228191", "dharmendratyagi87@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "MOHIT GANGWAR", "9336177550", "mohitgangwar@gmail.com"),
            FacultyContactsData(icon = Icons.Default.Person,"HOD/HOI", "DILEEP JIGYASI", "8435113459", "dileep.jigyasi2013@gmail.com"),
        )
    }

    val filteredFaculty = remember(searchQuery, filterType, allFaculty) {
        var result = allFaculty

        if (filterType != "All") {
            result = result.filter { it.profaction == filterType }
        }

        if (searchQuery.isNotBlank()) {
            result = result.filter { faculty ->
                faculty.name.contains(searchQuery, ignoreCase = true) ||
                        faculty.email.contains(searchQuery, ignoreCase = true) ||
                        faculty.number.contains(searchQuery, ignoreCase = true) ||
                        faculty.profaction.contains(searchQuery, ignoreCase = true)
            }
        }

        result
    }

    if (filteredFaculty.isEmpty()) {
        EmptyStateView(searchQuery)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatsCard(
                    totalCount = filteredFaculty.size,
                    filterType = filterType
                )
            }

            items(filteredFaculty.size) { index ->
                EnhancedFacultyCard(
                    faculty = filteredFaculty[index],
                    context = context,
                    index = index
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatsCard(totalCount: Int, filterType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when(filterType) {
                        "All" -> "Total Faculty Members"
                        "HOD/HOI" -> "Department Heads"
                        else -> "Teaching Staff"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "$totalCount Members",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
            }
            Text(
                text = "No Faculty Found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (searchQuery.isNotBlank())
                    "Try searching with different keywords"
                else
                    "No results match your filter",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EnhancedFacultyCard(
    faculty: FacultyContactsData,
    context: Context,
    index: Int
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Gradient color combinations for light and dark mode
    val gradientColorsLight = listOf(
        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
        listOf(Color(0xFFEC4899), Color(0xFFF59E0B)),
        listOf(Color(0xFF10B981), Color(0xFF06B6D4)),
        listOf(Color(0xFFF59E0B), Color(0xFFEF4444))
    )

    val gradientColorsDark = listOf(
        listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)),
        listOf(Color(0xFFDB2777), Color(0xFFD97706)),
        listOf(Color(0xFF059669), Color(0xFF0891B2)),
        listOf(Color(0xFFD97706), Color(0xFFDC2626))
    )

    val selectedGradients = if (isDarkTheme) gradientColorsDark else gradientColorsLight
    val gradientColors = remember(index) { selectedGradients[index % selectedGradients.size] }
    val isHOD = faculty.profaction == "HOD/HOI"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
        ) {
            // Background decoration circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = 80.dp.toPx(),
                    center = Offset(size.width * 0.9f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = 60.dp.toPx(),
                    center = Offset(size.width * 0.1f, size.height * 0.85f)
                )
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Avatar
                    Box {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(80.dp),
                            shadowElevation = 8.dp
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        // HOD Badge
                        if (isHOD) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFFFD700),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(24.dp),
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "HOD",
                                        tint = Color(0xFF1F2937),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = faculty.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Role Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = faculty.profaction,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Phone Info
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = faculty.number,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.95f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Divider(
                    color = Color.White.copy(alpha = 0.3f),
                    thickness = 1.dp
                )

                // Email
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = faculty.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Contact Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${faculty.email}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.25f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Email",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${faculty.number}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = gradientColors[0]
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Call",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = gradientColors[0]
                        )
                    }
                }
            }
        }
    }
}

data class FacultyContactsData(
    val icon: ImageVector,
    val profaction: String,
    val name: String,
    val number: String,
    val email: String,
)