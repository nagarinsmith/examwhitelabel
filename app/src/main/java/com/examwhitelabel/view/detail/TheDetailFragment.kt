package com.examwhitelabel.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.examwhitelabel.R
import com.examwhitelabel.TheDetailBinding
import com.examwhitelabel.view.shared.TheBaseFragment
import com.examwhitelabel.view.shared.consume
import com.examwhitelabel.view.shared.exhaustive
import com.examwhitelabel.view.shared.isNetworkAvailable
import com.examwhitelabel.view.shared.observeNonNull
import com.examwhitelabel.view.shared.registerNetworkCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheDetailFragment : TheBaseFragment<TheDetailBinding, TheDetailViewModel>(R.layout.fragment_detail, TheDetailViewModel::class) {

    private val navArgs: TheDetailFragmentArgs by navArgs()
    override val viewModelParams: Array<Any?> get() = arrayOf(navArgs.item)

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as? AppCompatActivity)?.supportActionBar?.title = navArgs.item.field1
        requireContext().registerNetworkCallback(onConnected = ::onNetworkConnected, onDisconnected = ::onNetworkDisconnected)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onNetworkConnected() {
        lifecycleScope.launch(Dispatchers.Main) {
            menu?.findItem(R.id.delete)?.isEnabled = true
        }
    }

    private fun onNetworkDisconnected() {
        lifecycleScope.launch(Dispatchers.Main) {
            menu?.findItem(R.id.delete)?.isEnabled = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.events.observeNonNull(viewLifecycleOwner, ::handleViewModelEvent)
    }

    private fun handleViewModelEvent(event: TheDetailViewModel.Event) = when (event) {
        TheDetailViewModel.Event.ItemDeleted -> findNavController().navigateUp()
    }.exhaustive()


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.item_menu, menu)
        if (!requireContext().isNetworkAvailable()) {
            menu.findItem(R.id.delete).isEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit -> consume {
            findNavController().navigate(TheDetailFragmentDirections.toAddOrEdit(navArgs.item))
        }
        R.id.delete -> consume {
            item.isEnabled = false
            viewModel.deleteItem()
        }
        else -> false
    }

    override fun onDestroy() {
        menu = null
        super.onDestroy()
    }
}
