package com.example.lesson20

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lesson20.databinding.FragmentBlankBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics

class BlankFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){map ->
        if (map.values.isNotEmpty() && map.values.all { it }){
            createNotification()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentBlankBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            FirebaseCrashlytics.getInstance().log("Log message with additional info")
            try {
                throw RuntimeException("Test Crash") // Force a crash
            }catch (e:Exception){
                FirebaseCrashlytics.getInstance().recordException(e)
            }
           createNotification()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun createNotification(){
        val intent = Intent(requireContext(), MainActivity::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notification = NotificationCompat.Builder(requireContext(), App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("My first notification")
            .setContentText("Description my first notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        if (REQUIRED_PERMISSIONS.all { permission->
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            }) {
            NotificationManagerCompat.from(requireContext()).notify(NOTIFICATION_ID, notification)
        } else{
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }

    companion object{
        private const val NOTIFICATION_ID = 1000
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }
}