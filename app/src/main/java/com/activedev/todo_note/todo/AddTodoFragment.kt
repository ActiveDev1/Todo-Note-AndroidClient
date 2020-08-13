package com.activedev.todo_note.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.activedev.todo_note.R
import com.activedev.todo_note.databinding.FragmentAddTodoBinding
import com.activedev.todo_note.network.TodoRequest
import com.shashank.sony.fancytoastlib.FancyToast
import java.text.SimpleDateFormat
import java.util.*


class AddTodoFragment : Fragment() {
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var sharedPref: SharedPreferences
    private var dueDate = ""
    private var todoText = ""
    private var date = ""
    private var time = ""
    private var isDone = 0
    private var isFavored = 0
    private val newDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_addTodoFragment_to_todoFragment3)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = activity?.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        )!!

        val token = sharedPref.getString(R.string.token.toString(), "")
        val request = TodoRequest(context!!, token)

        binding.saveTodo.setOnClickListener {
            todoText = binding.todoText.text.toString()

            if (todoText.isNotEmpty()) {
                if (dueDate.isNotEmpty()) {
                    request.createTodo(todoText, dueDate, isDone.toString(), isFavored.toString())
                } else
                    FancyToast.makeText(
                        context,
                        "Set a due date",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.WARNING, false
                    ).show()
            } else
                FancyToast.makeText(
                    context,
                    "Input a task name",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING, false
                ).show()
        }

        binding.rlDueDate.setOnClickListener {
            if (dueDate == "") {
                datePicker()
            } else {
                dueDate = ""
                binding.dueDate.text = "No reminder set"
                binding.imgDate.setSvgColor(R.color.txtColor)
            }
        }

        binding.rlIsFavored.setOnClickListener {

            isFavored = if (isFavored == 0) {
                binding.imgHighlight.setImageResource(R.drawable.ic_star_on)
                1
            } else {
                binding.imgHighlight.setImageResource(R.drawable.ic_star_off)
                0
            }
        }

        binding.rlIsDone.setOnClickListener {

            if (isDone == 0) {
                binding.imgDone.setImageResource(R.drawable.ic_is_done)
                isDone = 1
                val c = Calendar.getInstance().time
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                binding.timeDone.visibility = View.VISIBLE
                binding.timeDone.text = "Completed on " + df.format(c)

            } else {
                binding.imgDone.setImageResource(R.drawable.ic_not_done)
                isDone = 0
                binding.timeDone.visibility = View.GONE
                binding.timeDone.text = ""
            }
        }
    }

    private fun datePicker() {
        val c: Calendar = Calendar.getInstance()
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            context?.let {
                DatePickerDialog(
                    it,
                    OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        date = "$year-$monthOfYear-${dayOfMonth + 1}"
                        timePicker(year, monthOfYear, dayOfMonth)
                    }, mYear, mMonth, mDay
                )
            }
        datePickerDialog?.datePicker?.minDate = System.currentTimeMillis()
        datePickerDialog?.show()
    }

    private fun timePicker(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val mHour: Int
        val mMinute: Int
        val c = Calendar.getInstance()
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                newDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                time = "$hourOfDay:$minute:00"
                dueDate = "$date $time"
                binding.dueDate.text = dueDate
                binding.imgDate.setSvgColor(R.color.imageOn)
            },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun ImageView.setSvgColor(@ColorRes color: Int) =
        setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)

}