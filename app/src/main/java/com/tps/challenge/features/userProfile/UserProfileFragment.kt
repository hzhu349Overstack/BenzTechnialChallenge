package com.tps.challenge.features.userProfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tps.challenge.TCApplication.R
import com.tps.challenge.application.TCApplication
import com.tps.challenge.features.model.ApiState
import com.tps.challenge.utils.createGenericViewModelFactory


class UserProfileFragment : Fragment() {
    private var id = -1
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var view: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id")
            Log.d(TAG, id.toString())
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view =  inflater.inflate(R.layout.fragment_user_profile, container, false)
        userProfileViewModel = ViewModelProvider(
            requireActivity(),
            createGenericViewModelFactory {
                val application = requireActivity().application as TCApplication
                val useCase = application.getAppComponent().userProfileUseCase
                UserProfileViewModel(useCase)
            }
        )[UserProfileViewModel::class.java]

        progressBar = view.findViewById(R.id.progress_bar_profile)

        userProfileViewModel.getUserProfile(id)
        observerUi()
        return view
    }

    private fun observerUi() {
        userProfileViewModel.userProfileLiveData.observe(viewLifecycleOwner) { userProfile ->
            when (userProfile) {
                ApiState.Empty -> {
                }
                is ApiState.Error -> {

                }
                ApiState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ApiState.Success -> {
                    Log.d(TAG, userProfile.data.toString())
                    progressBar.visibility = View.GONE
                    view.findViewById<TextView>(R.id.id).text = userProfile.data.id.toString()
                    view.findViewById<TextView>(R.id.name).text = userProfile.data.name ?: "N/A"
                    view.findViewById<TextView>(R.id.company).text = userProfile.data.company ?: "N/A"
                    view.findViewById<TextView>(R.id.location).text = userProfile.data.location ?: "N/A"
                    view.findViewById<TextView>(R.id.followers).text = userProfile.data.followers.toString() ?: "N/A"
                    view.findViewById<TextView>(R.id.following).text = userProfile.data.following.toString() ?: "N/A"
                    view.findViewById<TextView>(R.id.public_repos).text = userProfile.data.public_repos.toString() ?: "N/A"
                    view.findViewById<TextView>(R.id.public_gists).text = userProfile.data.public_gists.toString() ?: "N/A"
                    view.findViewById<TextView>(R.id.twitter).text = userProfile.data.twitter_username ?: "N/A"
                    Glide.with(this)
                        .load(userProfile.data.avatar_url)
                        .into(view.findViewById(R.id.image_view_profile))
                }
            }

        }
    }

    companion object {
        const val TAG = "UserProfileFragment"
    }
}