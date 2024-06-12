package ai.pubtech.pubconsent_android_kotlin_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ai.pubtech.pubconsent_android_kotlin_example.ui.theme.PubconsentandroidkotlinexampleTheme
import ai.pubtech.pubconsent.Cmp
import ai.pubtech.pubconsent.CmpCallbacks
import ai.pubtech.pubconsent.CmpConfig
import ai.pubtech.pubconsent.dto.GoogleConsentModeStatus
import ai.pubtech.pubconsent.dto.GoogleConsentModeType
import ai.pubtech.pubconsent.ui.CmpUIConfig
import ai.pubtech.pubconsent.utility.CmpLogger
import ai.pubtech.pubconsent_android_kotlin_example.ui.theme.Orange
import ai.pubtech.pubconsent_android_kotlin_example.ui.theme.Purple40
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    var cmpInstance: Cmp? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //CmpUIConfig.fragmentContainerId = android.R.id.content

        CmpUIConfig.configureCenterScreen(this)

        cmpInstance = Cmp.createInstance(this)

        val cmp: Cmp = cmpInstance as Cmp

        val cmpCallbacks = CmpCallbacks(
            {
                CmpLogger.d("Consent ready")
                CmpLogger.d("Vendor expo is enabled: ${cmp.isVendorConsentEnabled(1)}")
                CmpLogger.d("Features: ${cmp.isFeatureCookiesEnabled()}")
                CmpLogger.d("User Experience: ${cmp.isUserExperienceCookiesEnabled()}")
                CmpLogger.d("Measurement: ${cmp.isMeasurementCookiesEnabled()}")
            },
            {
                CmpLogger.d("CMP closed")
            },
            {
                CmpLogger.d("CMP ui opened")
            },
            {
                CmpLogger.d("CMP error: $it")
            },
            { consentMap ->
                val firebaseConsentMap = consentMap.entries.associate { entry ->
                    val firebaseConsentType = when (entry.key) {
                        GoogleConsentModeType.ANALYTICS_STORAGE -> "FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE"
                        GoogleConsentModeType.AD_STORAGE -> "FirebaseAnalytics.ConsentType.AD_STORAGE"
                        GoogleConsentModeType.AD_USER_DATA -> "FirebaseAnalytics.ConsentType.AD_USER_DATA"
                        GoogleConsentModeType.AD_PERSONALIZATION -> "FirebaseAnalytics.ConsentType.AD_PERSONALIZATION"
                    }

                    val firebaseConsentStatus = when (entry.value) {
                        GoogleConsentModeStatus.GRANTED -> "FirebaseAnalytics.ConsentStatus.GRANTED"
                        GoogleConsentModeStatus.DENIED -> "FirebaseAnalytics.ConsentStatus.DENIED"
                    }

                    firebaseConsentType to firebaseConsentStatus
                }

                CmpLogger.d("Firebase mapping $firebaseConsentMap")
            }
        )

        // Please change the "id" and the "appName" this are used only for our demo purpose.
        // You can get the "id" from our Configurator.
        // The "appName" is something you have to choose here and don't change unless you have the purpose of doing that. (The appName will be useful for searching report date in our dashboard)
        val cmpConfig = CmpConfig("362", appName = "demo-app-android", cmpCallbacks, true)

        setContent {
            PubconsentandroidkotlinexampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Orange) { it ->
                    PageExample({ cmp.askConsent(this) }, { print("Google Vendor Status Enabled: ${cmp.isVendorConsentEnabled(755)}") }, it)
                }
            }
        }

        cmp.run(this, cmpConfig)
    }
}

@Composable
fun PageExample(openCmpCallback: () -> Unit, debugInfoCallback: () -> Unit, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFFF4F00)),
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp).fillMaxHeight(0.3f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "PubConsent CMP",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Android",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Simple")
                    Text(text = "Preview")
                }
            }

            Column(
                modifier = Modifier.padding(top = 100.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Demo Actions:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = { openCmpCallback() },
                    colors = ButtonDefaults.buttonColors(Purple40),
                    modifier = Modifier
                        .background(Purple40, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Open CMP",
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        debugInfoCallback()
                    },
                    colors = ButtonDefaults.buttonColors(Purple40),
                    modifier = Modifier
                        .background(Purple40, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Print Info",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PubconsentandroidkotlinexampleTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            PageExample(
                { print("Hi, it's a preview! You clicked the Open CMP button") },
                { print("Hi, it's a preview! You clicked the Debug Info button") },
                innerPadding
            )
        }
    }
}