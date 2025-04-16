document.addEventListener('DOMContentLoaded', function() {
    // Modal functionality
    const modal = document.getElementById('enrollment-modal');
    const viewButtons = document.querySelectorAll('.view-btn');
    const closeModal = document.querySelector('.close-modal');
    const closeBtn = document.querySelector('.modal-actions .close-btn');

    // Open modal when view button is clicked
    viewButtons.forEach(button => {
        button.addEventListener('click', function() {
            modal.style.display = 'block';
            // Here you would typically fetch and populate the enrollment details
            // For now we're using static data
        });
    });

    // Close modal
    closeModal.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    closeBtn.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    // Close modal when clicking outside
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Filter functionality
    const courseFilter = document.getElementById('course-filter');
    const dateFilter = document.getElementById('date-filter');
    const searchInput = document.getElementById('enrollment-search');
    const tableRows = document.querySelectorAll('.enrollment-table tbody tr');

    function filterEnrollments() {
        const courseValue = courseFilter.value;
        const dateValue = dateFilter.value;
        const searchValue = searchInput.value.toLowerCase();

        tableRows.forEach(row => {
            const courseCell = row.cells[2].textContent;
            const dateCell = row.cells[3].textContent;
            const studentCell = row.cells[1].textContent.toLowerCase();

            const courseMatch = courseValue === 'all' || courseCell.includes(courseFilter.options[courseFilter.selectedIndex].text);
            const dateMatch = dateValue === 'all' || checkDateMatch(dateCell, dateValue);
            const searchMatch = studentCell.includes(searchValue) ||
                courseCell.toLowerCase().includes(searchValue) ||
                row.cells[0].textContent.toLowerCase().includes(searchValue);

            if (courseMatch && dateMatch && searchMatch) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    function checkDateMatch(dateString, filterValue) {
        const enrollmentDate = new Date(dateString);
        const today = new Date();

        switch(filterValue) {
            case 'today':
                return enrollmentDate.toDateString() === today.toDateString();
            case 'week':
                const weekStart = new Date(today);
                weekStart.setDate(today.getDate() - today.getDay());
                return enrollmentDate >= weekStart;
            case 'month':
                return enrollmentDate.getMonth() === today.getMonth() &&
                    enrollmentDate.getFullYear() === today.getFullYear();
            default:
                return true;
        }
    }

    // Event listeners for filters
    courseFilter.addEventListener('change', filterEnrollments);
    dateFilter.addEventListener('change', filterEnrollments);
    searchInput.addEventListener('input', filterEnrollments);

    // Delete button functionality
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const row = this.closest('tr');
            if (confirm('Are you sure you want to remove this enrollment?')) {
                row.remove();
                // In a real application, you would also make an API call to delete the enrollment
            }
        });
    });

    // Pagination functionality
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');

    // In a real application, you would implement pagination logic here
    // This is just a basic example
    prevBtn.addEventListener('click', function() {
        alert('Previous page would load here');
    });

    nextBtn.addEventListener('click', function() {
        alert('Next page would load here');
    });

    // Export functionality
    const exportBtn = document.querySelector('.export-btn');
    exportBtn.addEventListener('click', function() {
        alert('Export to CSV functionality would be implemented here');
        // In a real application, you would generate a CSV file from the enrollment data
    });
});