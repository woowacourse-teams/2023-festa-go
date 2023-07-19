package com.festago.festago.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.databinding.ActivityMainBinding
import com.festago.festago.presentation.model.TicketUiModel
import com.festago.festago.presentation.ui.ticketentry.TicketEntryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory(
            TicketDefaultRepository(
                ticketRetrofitService = RetrofitClient.getInstance().ticketRetrofitService,
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initObserve()
        initView()
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
    }

    private fun initObserve() {
        vm.event.observe(this) {
            handleEvent(it)
        }
        vm.ticket.observe(this) {
            MainBinding.setBtnEnterTicket(binding, it)
            MainBinding.setTvTicketState(binding, it)
        }
    }

    private fun initView() {
        vm.loadTicket()
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is MainEvent.OpenTicketEntry -> navigateToTicketEntryActivity(event.ticketUiModel)
        is MainEvent.FailToOpenTicketEntry -> Toast.makeText(this, "Fail to open ticket entry", Toast.LENGTH_SHORT).show()
        is MainEvent.FailToLoadTicket -> Toast.makeText(this, "Fail to load ticket", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToTicketEntryActivity(ticketUiModel: TicketUiModel) {
        startActivity(TicketEntryActivity.getIntent(this, ticketUiModel.id))
    }
}
