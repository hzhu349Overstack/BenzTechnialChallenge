package com.tps.challenge.features.userFeed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tps.challenge.TCApplication.R
import com.tps.challenge.application.TCApplication
import com.tps.challenge.data.model.User
import com.tps.challenge.features.model.ApiState
import com.tps.challenge.features.userProfile.UserProfileFragment
import com.tps.challenge.utils.createGenericViewModelFactory

class UserFeedFragment : Fragment() {
    companion object {
        val TAG = "UserFeedFragment"
    }
    private lateinit var emptyMessage: TextView
    private lateinit var errorMessage: TextView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var userFeedViewModel: UserFeedViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_user_feed, container, false)
        userFeedViewModel = ViewModelProvider(
            requireActivity(),
            createGenericViewModelFactory {
                val application = requireActivity().application as TCApplication
                val useCase = application.getAppComponent().fetchAllUserUseCase
                UserFeedViewModel(useCase)
            }
        )[UserFeedViewModel::class.java]

        swipeRefreshLayout = view.findViewById(R.id.swipe_container)
        recyclerView = view.findViewById(R.id.employee_view)
        progressBar = view.findViewById(R.id.progress_bar)
        emptyMessage = view.findViewById(R.id.empty_text)
        errorMessage = view.findViewById(R.id.error_text)
        usersAdapter = UsersAdapter{ id->
            val userProfileFragment = UserProfileFragment()
            val bundle = Bundle()
            bundle.putInt("id", id)
            userProfileFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, userProfileFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
            setHasFixedSize(true)
        }

        subscribeUi()

        return view
    }

    private fun subscribeUi() {
        userFeedViewModel.allUserLiveData.observe(viewLifecycleOwner){ users->
            when(users){
                ApiState.Empty -> {
                    recyclerView.visibility = View.GONE
                    emptyMessage.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }
                is ApiState.Error -> {
                    recyclerView.visibility = View.GONE
                    emptyMessage.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }
                ApiState.Loading -> {
                    recyclerView.visibility = View.GONE
                    emptyMessage.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    errorMessage.visibility = View.GONE
                }
                is ApiState.Success -> {
                    recyclerView.visibility = View.VISIBLE
                    emptyMessage.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    Log.d(TAG, "data size: ${users.data.size}")
                    usersAdapter.users = (users.data) as List<User>
                }
            }
        }
    }
}