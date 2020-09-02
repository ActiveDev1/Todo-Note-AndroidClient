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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.activedev.todo_note.AlarmReceiver
import com.activedev.todo_note.AlarmScheduler
import com.activedev.todo_note.R
import com.activedev.todo_note.database.ReminderDatabase
import com.activedev.todo_note.database.ReminderDatabase.Companion.destroyInstance
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
    private var reminder = 0
    private var reminderType = "Notification"
    private var priority = "None"
    private var isDone = 0
    private var isFavored = 0
    private val newDate = Calendar.getInstance()
//    private val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


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
                    findNavController().navigate(R.id.action_addTodoFragment_to_todoFragment)
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
                    request.createTodo(
                        todoText,
                        dueDate,
                        isDone.toString(),
                        isFavored.toString()
                    )

                    if (reminder == 1) {

                        val reminderId =
                            ReminderDatabase.getInstance(context!!).reminderDatabaseDao.lastReminderId()
                                .toInt() + 1

                        val time: Long = newDate.timeInMillis
                        AlarmScheduler.setReminder(
                            requireContext(),
                            AlarmReceiver::class.java,
                            todoText,
                            time,
                            reminderId,
                            reminderType,
                            priority
                        )
                    }

                    ReminderDatabase.getInstance(context!!).reminderDatabaseDao.getAllTodo()
                    destroyInstance()

                    FancyToast.makeText(
                        context,
                        "Todo added",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS, false
                    ).show()
                    findNavController().navigate(R.id.action_addTodoFragment_to_todoFragment)

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
                    "Set a task name",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING, false
                ).show()
        }

        binding.rlDueDate.setOnClickListener {
            if (dueDate == "") {
                datePicker()
            } else {
                dueDate = ""
                binding.dueDate.text = getString(R.string.no_reminder_set)
                binding.imgDate.setSvgColor(R.color.txtColor)
                if (reminder == 1) {
                    reminder = 0
                    binding.rlReminderType.visibility = View.GONE
                    binding.imgReminder.setImageResource(R.drawable.ic_reminder_off)
                    binding.reminder.text = getString(R.string.don_t_remind_me)
                }
            }
        }

        binding.rlReminder.setOnClickListener {
            if (dueDate == "") {
                FancyToast.makeText(
                    context,
                    "Tell me when set a date and time first.",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING, false
                ).show()
            } else if (dueDate.isNotEmpty() && reminder == 1) {
                reminder = 0
                binding.rlReminderType.visibility = View.GONE
                binding.imgReminder.setImageResource(R.drawable.ic_reminder_off)
                binding.reminder.text = getString(R.string.don_t_remind_me)
            } else if (dueDate.isNotEmpty() && reminder == 0) {
                reminder = 1
                binding.rlReminderType.visibility = View.VISIBLE
                binding.imgReminder.setImageResource(R.drawable.ic_reminder_on)
                binding.reminder.text = getString(R.string.remind_me_when_due)
                binding.imgReminderType.setSvgColor((R.color.imageOn))
            }
        }

        binding.rlReminderType.setOnClickListener {
            showReminderTypeDialog()
        }

        binding.rlPriority.setOnClickListener {
            showPriorityDialog()
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
                binding.rlReminderType.visibility = View.GONE
//                binding.timeDone.text = "Not Complete"
            }
        }
    }

    private fun datePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        mYear = currentDate.get(Calendar.YEAR)
        mMonth = currentDate.get(Calendar.MONTH)
        mDay = currentDate.get(Calendar.DAY_OF_MONTH)

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
        val currentTime = Calendar.getInstance()
        mHour = currentTime.get(Calendar.HOUR_OF_DAY)
        mMinute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                newDate.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0)

                if (newDate <= currentTime) {
                    FancyToast.makeText(context, "Invalid Time", FancyToast.LENGTH_LONG).show()
                } else {
                    time = if (minute < 9)
                        "$hourOfDay:0$minute:00"
                    else
                        "$hourOfDay:$minute:00"
                    dueDate = "$date $time"
                    binding.dueDate.text = dueDate
                    binding.imgDate.setSvgColor(R.color.imageOn)
                    binding.imgReminder.setImageResource(R.drawable.ic_reminder_on)
                    reminder = 1
                    binding.reminder.text = getString(R.string.remind_me_when_due)
                    binding.rlReminderType.visibility = View.VISIBLE
                    binding.imgReminderType.setSvgColor((R.color.imageOn))
                }
            },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun showReminderTypeDialog() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Reminder Type")

        val items =
            arrayOf("Notification", "Alarm", "None")
        var checkedItem = 0
        when (binding.reminderType.text) {
            "Notification" -> checkedItem = 0
            "Alarm" -> checkedItem = 1
            "None" -> checkedItem = 2
        }

        builder?.setSingleChoiceItems(
            items, checkedItem
        ) { dialog, which ->
            when (which) {
                0 -> {
                    reminderType = "Notification"
                    binding.reminderType.text = getString(R.string.notification)
                    dialog.cancel()
                }
                1 -> {
                    reminderType = "Alarm"
                    binding.reminderType.text = getString(R.string.alarm)
                    dialog.cancel()
                }
                2 -> {
                    reminderType = "None"
                    binding.reminderType.text = getString(R.string.none)
                    dialog.cancel()
                }
            }
        }
        builder?.setNegativeButton("Cancel", null)
        val dialog: AlertDialog = builder!!.create()
        dialog.show()
    }

    private fun showPriorityDialog() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Task Priority")

        val items =
            arrayOf("High", "Medium", "Low", "None")
        var checkedItem = 0
        when (binding.priority.text) {
            "High" -> checkedItem = 0
            "Medium" -> checkedItem = 1
            "Low" -> checkedItem = 2
            "None" -> checkedItem = 3
        }

        builder?.setSingleChoiceItems(
            items, checkedItem
        ) { dialog, which ->
            when (which) {
                0 -> {
                    priority = "High"
                    binding.priority.text = getString(R.string.high)
                    binding.imgPriority.setSvgColor(R.color.high)
                    dialog.cancel()
                }
                1 -> {
                    priority = "Medium"
                    binding.priority.text = getString(R.string.medium)
                    binding.imgPriority.setSvgColor(R.color.medium)
                    dialog.cancel()
                }
                2 -> {
                    priority = "Low"
                    binding.priority.text = getString(R.string.low)
                    binding.imgPriority.setSvgColor(R.color.low)
                    dialog.cancel()
                }
                3 -> {
                    priority = "None"
                    binding.priority.text = getString(R.string.none)
                    binding.imgPriority.setSvgColor(R.color.black)
                    dialog.cancel()
                }
            }
        }
        builder?.setNegativeButton("Cancel", null)
        val dialog: AlertDialog = builder!!.create()
        dialog.show()
    }

    private fun ImageView.setSvgColor(@ColorRes color: Int) =
        setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)

}