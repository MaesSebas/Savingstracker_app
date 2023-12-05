package be.ucll.tasklist

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import be.ucll.tasklist.databinding.LoginFragmentLoginBinding

class Login__FragmentLogin : Fragment() {
    private var _binding: LoginFragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Login__FragmentLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(Login__FragmentLoginViewModel::class.java)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.numpadBtnClearCode.setOnClickListener {
            viewModel.clearCode()
        }

        binding.numpadBtnLogin.setOnClickListener {
            viewModel.checkCodeAndRedirectIfCorrect()
        }

        viewModel.enteredDigits.observe(viewLifecycleOwner) { digits ->
            updateCircleViews(digits, view)
        }

        viewModel.codeIncorrect.observe(viewLifecycleOwner) { isIncorrect ->
            if (isIncorrect) {
                Toast.makeText(
                    requireContext(),
                    "Incorrect code. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (!isIncorrect) {
                findNavController().navigate(R.id.cards)
            }
        }
        return view
    }

    fun updateCircleViews(digits: Int, view: View) {
        for (i in 1..4) {
            val circleView = view.findViewById<View>(view.resources.getIdentifier("representationCodeDigit$i", "id", view.context.packageName))
            val drawable = circleView.background as GradientDrawable

            if (i <= digits) {
                drawable.setColor(Color.DKGRAY)
            } else {
                drawable.setColor(Color.GRAY)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}