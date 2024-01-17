package com.festago.festago.di

import com.festago.festago.presentation.ui.ticketentry.TicketEntryActivity
import com.festago.festago.presentation.ui.ticketentry.GetTicketEntryActivityIntentInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object IntentModule {
    @Provides
    fun provideGetTicketEntryActivityIntent(): GetTicketEntryActivityIntentInterface =
        TicketEntryActivity.Companion.GetTicketEntryActivityIntent
}
