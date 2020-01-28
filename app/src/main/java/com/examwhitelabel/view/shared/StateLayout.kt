package com.examwhitelabel.view.shared

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.annotation.LayoutRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.examwhitelabel.NetworkErrorStateBinding
import com.examwhitelabel.R
import com.examwhitelabel.view.shared.StateLayout.State

/**
 * View which helps with view state management (loading, loaded, error cases). It internally maps a [State] to it's view and sets up any required bindings.
 * To add/change states, define them in [State], map and handle them in `State change handling` region and define their afferent views in the companion object.
 */
class StateLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    @LayoutRes
    private var emptyLayout: Int = 0
    @LayoutRes
    private var loadingLayout: Int = 0

    private var state: State? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var stateChangeListener: ((State) -> Unit)? = null

    @Suppress("MemberVisibilityCanBePrivate")
    var animationEnabled = true
    private lateinit var showAnimation: Animation
    private lateinit var hideAnimation: Animation

    private lateinit var contentView: View

    @Suppress("MemberVisibilityCanBePrivate")
    var stateView: View? = null
        private set

    init {
        context.withStyledAttributes(attrs, R.styleable.StateLayout) {
            emptyLayout = getResourceId(R.styleable.StateLayout_state_empty_layout, INVALID_LAYOUT)
            loadingLayout = getResourceId(R.styleable.StateLayout_state_loading_layout, LOADING_LAYOUT)
            animationEnabled = getBoolean(R.styleable.StateLayout_initial_animation, true)
        }
        initializeAnimations()
    }

    private fun initializeAnimations() {
        showAnimation = AlphaAnimation(0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = ANIMATION_DURATION / 2L
        }
        hideAnimation = AlphaAnimation(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = ANIMATION_DURATION / 2L
        }
    }

    /**
     * Hides the normal view after initial inflation and calls [updateStateView] which loads the view for the defined initial [State]
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = getChildAt(0) ?: throw NullPointerException("No content view available")
        contentView.isVisible = false
        updateStateView()
    }

    @Suppress("unused")
    fun onStateChange(listener: (State) -> Unit) {
        stateChangeListener = listener
    }

    @Suppress("unused")
    fun clearStateListener() {
        stateChangeListener = null
    }

    @Suppress("unused")
    @Throws(NullPointerException::class)
    fun setState(state: State?) {
        val currentState = this.state
        if (state == null || currentState != null && state::class == currentState::class) return
        this.state = state
        updateStateView()
    }

    // region State change handling
    @Throws(InvalidViewStateException::class)
    private fun handleStateChange() {
        when (val newState = state) {
            State.Normal -> showNormalState()
            State.Empty -> showStateView(emptyLayout)
            State.Loading -> showStateView(loadingLayout)
            is State.Error.NetworkError -> showErrorState(newState)
            State.Invalid -> throw InvalidViewStateException()
            null -> Unit
        }.exhaustive()
    }

    @Throws(InvalidViewStateException::class)
    private fun showStateView(@LayoutRes layout: Int) {
        if (layout == INVALID_LAYOUT) throw InvalidViewStateException(
            "No layout provided for state: $state"
        )

        stateView = inflater.inflate(layout, this, false).also { stateView ->
            (stateView.layoutParams as LayoutParams).gravity = Gravity.CENTER
            addView(stateView)

            if (animationEnabled) {
                showAnimation.fillAfter = true
                showAnimation.setAnimationListener(null)
                stateView.startAnimation(showAnimation)
            }

            if (contentView.isVisible) {
                if (animationEnabled) {
                    hideAnimation.setAnimationListener(object : AnimationListenerAdapter {
                        override fun onAnimationEnd(p0: Animation?) {
                            contentView.isVisible = false
                        }
                    })
                    contentView.startAnimation(hideAnimation)
                } else {
                    contentView.isVisible = false
                }
            }
        }
    }

    private fun showNormalState() {
        if (animationEnabled) {
            showAnimation.fillAfter = true
            showAnimation.setAnimationListener(object : AnimationListenerAdapter {
                override fun onAnimationStart(p0: Animation?) {
                    contentView.isVisible = true
                }
            })
            contentView.clearAnimation()
            contentView.startAnimation(showAnimation)
        } else {
            contentView.isVisible = true
        }
        stateView = null
    }

    private fun showErrorState(errorState: State.Error) {
        when (errorState) {
            is State.Error.NetworkError -> {
                showStateView(NETWORK_ERROR_LAYOUT)
                stateView?.let {
                    NetworkErrorStateBinding.bind(it).let { networkErrorStateBinding ->
                        networkErrorStateBinding.tryAgain.setOnClickListener {
                            errorState.retryAction()
                        }
                    }
                }
            }
        }.exhaustive()
    }
    // endregion State change handling

    /**
     * Updates the view with the current [State]
     */
    private fun updateStateView() {
        stateView?.let { stateView ->
            if (animationEnabled) {
                hideAnimation.setAnimationListener(object : AnimationListenerAdapter {
                    override fun onAnimationEnd(p0: Animation?) {
                        removeView(stateView)
                        handleStateChange()
                    }
                })
                stateView.startAnimation(hideAnimation)
            } else {
                removeView(stateView)
                handleStateChange()
            }
        } ?: handleStateChange()
    }

    class InvalidViewStateException(message: String? = null) :
        IllegalStateException(message ?: "Invalid or unspecified view state, use one of the specified states.")

    /**
     * Available view states for [StateLayout]
     */
    @Keep
    sealed class State {

        object Invalid : State()
        object Normal : State()
        object Empty : State()
        object Loading : State()
        sealed class Error : State() {
            data class NetworkError(val retryAction: () -> Unit) : Error()
        }
    }

    companion object {

        private const val ANIMATION_DURATION = 300

        private const val INVALID_LAYOUT = -1
        private const val LOADING_LAYOUT = R.layout.view_state_loading
        private const val NETWORK_ERROR_LAYOUT = R.layout.view_state_network_error

        private interface AnimationListenerAdapter : Animation.AnimationListener {

            override fun onAnimationStart(p0: Animation?) = Unit
            override fun onAnimationEnd(p0: Animation?) = Unit
            override fun onAnimationRepeat(p0: Animation?) = Unit
        }
    }
}
