package com.example.mazzadytask.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.example.mazzadytask.domain.state.StateData
import com.example.mazzadytask.presentation.utils.ObserverCallbacks


abstract class BaseFragment<VBinding : ViewBinding> : Fragment() {

    private var _binding: VBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding get() = _binding!!
    private val observers get() = screenDataObservers()
    protected var progressView: View? = null

    //Init view binding
    protected abstract fun getViewBinding(): VBinding

    //Init UI and views
    protected abstract fun setUpViews()

    //Init livedata observers
    protected open fun screenDataObservers(): List<ObserverCallbacks<*>> = listOf()


    override fun onDestroyView() {
        //  view?.hideKeyboard()
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        //to avoid creating double observers in same screen
        for (observer in observers) {
            observer.liveData.removeObservers(this)
            attachObserver(observer)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setUpViews()
    }

    fun getBaseFragmentReference(): BaseFragment<VBinding> {
        return this
    }

    private fun init() {
        _binding = getViewBinding()
    }

    private fun <T> attachObserver(
        observerData: ObserverCallbacks<T>
    ) {
        val observer: Observer<StateData<T>> = createObserver(observerData)
        observerData.liveData.removeObserver(observer)
        observerData.liveData.observe(viewLifecycleOwner, observer)
    }

    private fun <T> createObserver(input: ObserverCallbacks<T>): Observer<StateData<T>> {
        return Observer<StateData<T>> { observerData ->
            when (observerData.status) {
                StateData.DataStatus.SUCCESS ->{
                    // Handle Success
                    progressView?.visibility = View.GONE
                    input.success.invoke(observerData.data)

            }
                StateData.DataStatus.ERROR -> {
                    // Handle Error
                    progressView?.visibility = View.GONE
                    input.error.invoke(observerData.error)
                }
                StateData.DataStatus.LOADING -> {
                    // Handle Loading
                    progressView?.visibility = View.VISIBLE
                    input.loading.invoke()
                }
                StateData.DataStatus.COMPLETE -> {
                    // Handle Complete
                    progressView?.visibility = View.GONE
                    input.complete.invoke()
                }
            }
        }
    }
}

//Show custom dialog
fun Fragment.showDialog(
    message: String
) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}