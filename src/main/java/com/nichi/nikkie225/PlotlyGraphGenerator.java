package com.nichi.nikkie225;

import java.util.List;

public class PlotlyGraphGenerator {

    public static String generateDMAHTML(List<String> dates, List<Double> dma5, List<Double> dma8,
                                         List<Double> dma13, List<Double> dma50, List<Double> dma200,
                                         List<Double> open, List<Double> high, List<Double> low, List<Double> close,
                                         String stockCode) {

        StringBuilder html = new StringBuilder();

        html.append("""
<html>
<head>
<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
    html, body {
        margin: 0;
        padding: 0;
        width: 100vw;
        height: 100vh;
        background-color: #111;
        color: #eee;
        font-family: Arial, sans-serif;
        overflow: hidden;
    }
    #chart {
        width: 100%;
        height: 100%;
    }
</style>
</head>
<body>
<div id="chart"></div>
<script>
let data = [];
""");


        html.append("var candleTrace = {\n")
                .append("  x: ").append(dates.toString()).append(",\n")
                .append("  open: ").append(open.toString()).append(",\n")
                .append("  high: ").append(high.toString()).append(",\n")
                .append("  low: ").append(low.toString()).append(",\n")
                .append("  close: ").append(close.toString()).append(",\n")
                .append("  type: 'candlestick',\n")
                .append("  name: 'OHLC',\n")
                .append("  increasing: { line: { color: '#00ff00' } },\n")
                .append("  decreasing: { line: { color: '#ff0000' } },\n")
                .append("  xaxis: 'x',\n")
                .append("  yaxis: 'y'\n")
                .append("};\n")
                .append("data.push(candleTrace);\n");


        appendTrace(html, "DMA 5", "red", dates, dma5);
        appendTrace(html, "DMA 8", "orange", dates, dma8);
        appendTrace(html, "DMA 13", "yellow", dates, dma13);
        appendTrace(html, "DMA 50", "blue", dates, dma50);
        appendTrace(html, "DMA 200", "green", dates, dma200);


        html.append("var layout = {\n")
                .append("  autosize: true,\n")
                .append("  title: {\n")
                .append("    text: 'DMA Graph - ").append(stockCode).append("',\n")
                .append("    font: { color: 'white', size: 24 },\n")
                .append("    xref: 'paper', x: 0\n")
                .append("  },\n")
                .append("  paper_bgcolor: '#111',\n")
                .append("  plot_bgcolor: '#111',\n")
                .append("  xaxis: {\n")
                .append("    title: { text: 'Date', font: { color: 'white' } },\n")
                .append("    color: 'white',\n")
                .append("    tickangle: -45,\n")
                .append("    type: 'category',\n")
                .append("    tickmode: 'linear',\n")
                .append("    tickfont: { size: 10 },\n")
                .append("    tickvals: Array.from(Array(").append(dates.size()).append(").keys()),\n")
                .append("    ticktext: ").append(dates.toString()).append(",\n")
                .append("    rangeslider: { visible: false }\n")
                .append("  },\n")
                .append("  yaxis: {\n")
                .append("    title: { text: 'Price / DMA Value', font: { color: 'white' } },\n")
                .append("    color: 'white',\n")
                .append("    tickformat: ',f'\n")
                .append("  },\n")
                .append("  legend: {\n")
                .append("    x: 1.02,\n")
                .append("    y: 1,\n")
                .append("    bgcolor: 'rgba(0,0,0,0)',\n")
                .append("    font: { color: 'white' }\n")
                .append("  },\n")
                .append("  hovermode: 'x unified',\n")
                .append("  hoverlabel: {\n")
                .append("    bgcolor: '#222',\n")
                .append("    bordercolor: '#888',\n")
                .append("    font: { color: 'white' }\n")
                .append("  },\n")
                .append("  margin: { l: 60, r: 40, t: 60, b: 80 }\n")
                .append("};\n");


        html.append("var config = {\n")
                .append("  responsive: true,\n")
                .append("  scrollZoom: true,\n")
                .append("  displaylogo: false,\n")
                .append("  displayModeBar: true,\n")
                .append("  modeBarButtonsToAdd: ['zoomIn2d', 'zoomOut2d', 'resetScale2d']\n")
                .append("};\n");


        html.append("Plotly.newPlot('chart', data, layout, config);\n")
                .append("</script>\n</body>\n</html>");

        return html.toString();
    }

    private static void appendTrace(StringBuilder html, String name, String color,
                                    List<String> dates, List<Double> values) {
        String traceName = name.replace(" ", "");
        html.append("var trace").append(traceName).append(" = {\n")
                .append("  x: ").append(dates.toString()).append(",\n")
                .append("  y: ").append(values.toString()).append(",\n")
                .append("  mode: 'lines',\n")
                .append("  name: '").append(name).append("',\n")
                .append("  line: { color: '").append(color).append("', width: 2 },\n")
                .append("  marker: { size: 5 },\n")
                .append("  hoverinfo: 'x+y+name'\n")
                .append("};\n")
                .append("data.push(trace").append(traceName).append(");\n");
    }
}
