package com.zeynelinho.artbooktesting.viewModel

import com.zeynelinho.artbooktesting.repo.ArtRepository
import com.zeynelinho.artbooktesting.repo.FakeArtRepository
import org.junit.Before

class ArtViewModelTest {

    private lateinit var viewModel : ArtViewModel

    @Before
    fun setup() {

        viewModel = ArtViewModel(FakeArtRepository())
    }

}