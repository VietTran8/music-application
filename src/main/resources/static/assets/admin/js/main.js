const now = new Date();
const currentMonth = now.getMonth() + 1;
const currentYear = now.getFullYear();

const monthLabels = ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"]
const annualRevenueData = {
    labels: monthLabels,
    datasets: [
        {
            label: "Doanh thu gói quảng cáo năm",
            borderColor: "blue",
            backgroundColor:"blue",
            data: [],
            tension: 0.4,
        },
        {
            label: "Doanh thu gói nâng cấp năm",
            borderColor: "red",
            backgroundColor:"red",
            data: [],
            tension: 0.4,
        },
    ],
}
const annualRevenueConfig = {
    type: "bar",
    data: annualRevenueData,
}

const monthlyRevenueData = {
    datasets: [
        {
            label: "Doanh thu gói quảng cáo tháng",
            borderColor: "blue",
            data: [],
            tension: 0.4,
        },
        {
            label: "Doanh thu gói nâng cấp tháng",
            borderColor: "red",
            // backgroundColor:"red",
            data: [],
            tension: 0.4,
        }
    ],
}
const monthlyRevenueConfig = {
    type: "line",
    data: monthlyRevenueData,
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

document.getElementById('date').value = formatDate(new Date());

function fetchStatistic(month, year){
    fetch(`/api/statistics/year/${year}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 200 && data.status) {
                const stats = data.data;

                const adsData = Object.values(stats.ADS);
                const upgradeData = Object.values(stats.UPGRADE_PACKAGE);

                annualRevenueChart.data.datasets[0].data = adsData;
                annualRevenueChart.data.datasets[1].data = upgradeData;
                annualRevenueChart.update();
            }
        })
        .catch(error => console.error('Error fetching annual statistics:', error));

    fetch(`/api/statistics/month/${month}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 200 && data.status) {
                const stats = data.data;

                const adsData = Object.values(stats.ADS);
                const upgradeData = Object.values(stats.UPGRADE_PACKAGE);

                monthlyRevenueChart.data.labels = ["Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5", "Tuần 6"].slice(0, Math.max(adsData.length, upgradeData.length));
                monthlyRevenueChart.data.datasets[0].data = adsData;
                monthlyRevenueChart.data.datasets[1].data = upgradeData;
                monthlyRevenueChart.update();
            }
        })
        .catch(error => console.error('Error fetching monthly statistics:', error));
}

const annualRevenue = document.getElementById('annualRevenue')
const monthlyRevenue = document.getElementById('monthlyRevenue')

const annualRevenueChart = new Chart(annualRevenue, annualRevenueConfig);
const monthlyRevenueChart = new Chart(monthlyRevenue, monthlyRevenueConfig);

annualRevenueChart.data.datasets.forEach(dataset => {
    if (dataset.label.includes("quảng cáo")) {
        dataset.label = `Doanh thu gói quảng cáo năm ${currentYear}`;
    } else if (dataset.label.includes("nâng cấp")) {
        dataset.label = `Doanh thu gói nâng cấp năm ${currentYear}`;
    }
});

monthlyRevenueChart.data.datasets.forEach(dataset => {
    if (dataset.label.includes("quảng cáo")) {
        dataset.label = `Doanh thu gói quảng cáo ${monthLabels[currentMonth - 1]}/${currentYear}`;
    } else if (dataset.label.includes("nâng cấp")) {
        dataset.label = `Doanh thu gói nâng cấp ${monthLabels[currentMonth - 1]}/${currentYear}`;
    }
});

fetchStatistic(currentMonth, currentYear);

document.getElementById('date').addEventListener('change', function() {
    let input = this.value;
    let date = new Date(input);

    if (!isNaN(date.getTime())) {
        let month = date.getMonth() + 1;
        let year = date.getFullYear();

        fetchStatistic(month, year);
    }
});