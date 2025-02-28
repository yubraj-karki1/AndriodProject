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
