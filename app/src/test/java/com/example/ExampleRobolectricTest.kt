package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AMPLA PERSONAL IA", appName)
  }

  @Test
  fun `bmi calculation test`() {
    val profile = UserProfile(weightKg = 70f, heightCm = 175f)
    assertEquals(22.9f, profile.bmi, 0.1f)
    assertEquals("Peso Saudável", profile.bmiCategory.label)
  }
}
