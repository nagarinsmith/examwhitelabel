package com.examwhitelabel.view.addOrEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.examwhitelabel.R
import com.examwhitelabel.TheAddOrEditBinding
import com.examwhitelabel.view.shared.TheBaseFragment
import com.examwhitelabel.view.shared.consume
import com.examwhitelabel.view.shared.exhaustive
import com.examwhitelabel.view.shared.isNetworkAvailable
import com.examwhitelabel.view.shared.observeNonNull
import com.examwhitelabel.view.shared.registerNetworkCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheAddOrEditFragment : TheBaseFragment<TheAddOrEditBinding, TheAddOrEditViewModel>(R.layout.layout_add_or_edit, TheAddOrEditViewModel::class) {

    private val navArgs: TheAddOrEditFragmentArgs by navArgs()
    override val viewModelParams: Array<Any?> get() = arrayOf(navArgs.item)

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireContext().registerNetworkCallback(onConnected = ::onNetworkConnected, onDisconnected = ::onNetworkDisconnected)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onNetworkConnected() {
        lifecycleScope.launch(Dispatchers.Main) {
            menu?.findItem(if (navArgs.item != null) R.id.save else R.id.add)?.isEnabled = true
        }
    }

    private fun onNetworkDisconnected() {
        lifecycleScope.launch(Dispatchers.Main) {
            menu?.findItem(if (navArgs.item != null) R.id.save else R.id.add)?.isEnabled = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.events.observeNonNull(viewLifecycleOwner, ::handleViewModelEvent)
    }

    private fun handleViewModelEvent(event: TheAddOrEditViewModel.Event) = when (event) {
        is TheAddOrEditViewModel.Event.ItemAdded -> findNavController().navigateUp().exhaustive()
        is TheAddOrEditViewModel.Event.ItemSaved -> findNavController().navigate(TheAddOrEditFragmentDirections.toDetail(event.item))
    }.exhaustive()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(if (navArgs.item != null) R.menu.save_action else R.menu.add_action, menu)
        if (!requireContext().isNetworkAvailable()) {
            menu.findItem(if (navArgs.item != null) R.id.save else R.id.add).isEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save -> consume {
            item.isEnabled = false
            viewModel.save()
        }
        R.id.add -> consume {
            item.isEnabled = false
            viewModel.add()
        }
        else -> false
    }

    override fun onDestroy() {
        menu = null
        super.onDestroy()
    }
}
