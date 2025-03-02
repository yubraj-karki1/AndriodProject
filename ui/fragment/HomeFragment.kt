import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

// ...existing code...
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Initialize ViewModel and Adapter
    val repo = ProductRepositoryImpl()
    taskViewModel = ProductViewModel(repo)
    adapter = ProductAdapter(requireContext(), ArrayList())

    // Set up RecyclerView
    binding.recycleView.adapter = adapter
    binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

    // Change swipe-to-delete to swipe-to-add-cart
    val swipeBackground = ColorDrawable(Color.GREEN)
    val cartIcon = requireContext().getDrawable(android.R.drawable.ic_input_add)
    
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT
    ) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val productId = adapter.getProductId(position)
            
            // Show loading indicator
            binding.progressBar.visibility = View.VISIBLE
            
            // Add to cart
            taskViewModel.addToCart(productId, "1")
            
            // Reset the item view
            adapter.notifyItemChanged(position)
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            
            // Draw green background
            swipeBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            swipeBackground.draw(c)
            
            // Draw cart icon
            cartIcon?.let {
                val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + it.intrinsicHeight
                val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                it.draw(c)
            }
            
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    })

    itemTouchHelper.attachToRecyclerView(binding.recycleView)

    // Observe tasks from ViewModel
    taskViewModel.allproducts.observe(viewLifecycleOwner) { tasks ->
        tasks?.let {
            adapter.updateData(it)
        }
    }

    // Observe loading state
    taskViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Observe delete message
    taskViewModel.deleteMessage.observe(viewLifecycleOwner) { message ->
        message?.let {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    // Add observer for cart message
    taskViewModel.cartMessage.observe(viewLifecycleOwner) { message ->
        message?.let {
            binding.progressBar.visibility = View.GONE
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                .setAction("View Cart") {
                    // Navigate to cart fragment
                    findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
                }
                .show()
        }
    }

    // Fetch all tasks
    taskViewModel.getAllProducts()
}

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    // Inflate the layout for this fragment
    binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
}
// ...existing code...
