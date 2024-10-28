package com.tps.challenge.features.userProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
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
        view = inflater.inflate(R.layout.fragment_user_profile, container, false)
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
                    view.findViewById<LinearLayout>(R.id.text_part).visibility = View.GONE
                }

                is ApiState.Success -> {
                    Log.d(TAG, userProfile.data.toString())

                    progressBar.visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.text_part).visibility = View.VISIBLE

                    setText(R.id.id, userProfile.data.id.toString())
                    setText(R.id.name, userProfile.data.name)
                    setText(R.id.company, userProfile.data.company)
                    setText(R.id.location, userProfile.data.location)
                    setText(R.id.followers, userProfile.data.followers?.toString())
                    setText(R.id.following, userProfile.data.following?.toString())
                    setText(R.id.public_repos, userProfile.data.public_repos?.toString())
                    setText(R.id.public_gists, userProfile.data.public_gists?.toString())
                    setText(R.id.twitter, userProfile.data.twitter_username)

                    Glide.with(this)
                        .load(userProfile.data.avatar_url)
                        .into(view.findViewById(R.id.image_view_profile))
                }
            }
        }
    }

    fun setText(viewId: Int, text: String?) {
        view.findViewById<TextView>(viewId).text = text ?: "N/A"
    }

    companion object {
        const val TAG = "UserProfileFragment"
    }
}