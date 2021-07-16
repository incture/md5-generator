package oneapp.incture.workbox.demo.dashboard.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class GraphConfigurationDto extends BaseDto {

	private String graphConfigId;

	private String userId;

	private String graphName;

	private String chartType;

	private Boolean gridView;

	private Boolean showLegends;

	private String xLabel;

	private String xParameter;

	private Boolean xScrollbar;

	private String xCategory;

	private String yLabel;

	private String yParameter;

	private Boolean yScrollbar;
	
	private Boolean yScrollBar;

	public Boolean getyScrollBar() {
		return yScrollBar;
	}

	public void setyScrollBar(Boolean yScrollBar) {
		this.yScrollBar = yScrollBar;
	}

	private String yCategory;

	private Integer sequence;

	private String frameDetail;

	private Boolean isActive;

	private String filterData;

	private List<String> xFilter;

	private List<String> yFilter;

	private Integer xAxisTopValue;

	private Integer yAxisTopValue;
	
	private String validForUsage;

	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}

	public Integer getxAxisTopValue() {
		return xAxisTopValue;
	}

	public void setxAxisTopValue(Integer xAxisTopValue) {
		this.xAxisTopValue = xAxisTopValue;
	}

	public Integer getyAxisTopValue() {
		return yAxisTopValue;
	}

	public void setyAxisTopValue(Integer yAxisTopValue) {
		this.yAxisTopValue = yAxisTopValue;
	}

	public List<String> getxFilter() {
		return xFilter;
	}

	public void setxFilter(List<String> xFilter) {
		this.xFilter = xFilter;
	}

	public List<String> getyFilter() {
		return yFilter;
	}

	public void setyFilter(List<String> yFilter) {
		this.yFilter = yFilter;
	}

	public String getFilterData() {
		return filterData;
	}

	public void setFilterData(String filterData) {
		this.filterData = filterData;
	}

	public String getGraphConfigId() {
		return graphConfigId;
	}

	public void setGraphConfigId(String graphConfigId) {
		this.graphConfigId = graphConfigId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public Boolean getGridView() {
		return gridView;
	}

	public void setGridView(Boolean gridView) {
		this.gridView = gridView;
	}

	public Boolean getShowLegends() {
		return showLegends;
	}

	public void setShowLegends(Boolean showLegends) {
		this.showLegends = showLegends;
	}

	public String getxLabel() {
		return xLabel;
	}

	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	public String getxParameter() {
		return xParameter;
	}

	public void setxParameter(String xParameter) {
		this.xParameter = xParameter;
	}

	public Boolean getxScrollbar() {
		return xScrollbar;
	}

	public void setxScrollbar(Boolean xScrollbar) {
		this.xScrollbar = xScrollbar;
	}

	public String getxCategory() {
		return xCategory;
	}

	public void setxCategory(String xCategory) {
		this.xCategory = xCategory;
	}

	public String getyLabel() {
		return yLabel;
	}

	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	public String getyParameter() {
		return yParameter;
	}

	public void setyParameter(String yParameter) {
		this.yParameter = yParameter;
	}

	public Boolean getyScrollbar() {
		return yScrollbar;
	}

	public void setyScrollbar(Boolean yScrollbar) {
		this.yScrollbar = yScrollbar;
	}

	public String getyCategory() {
		return yCategory;
	}

	public void setyCategory(String yCategory) {
		this.yCategory = yCategory;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getFrameDetail() {
		return frameDetail;
	}

	public void setFrameDetail(String frameDetail) {
		this.frameDetail = frameDetail;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "GraphConfigurationDto [graphConfigId=" + graphConfigId + ", userId=" + userId + ", graphName="
				+ graphName + ", chartType=" + chartType + ", gridView=" + gridView + ", showLegends=" + showLegends
				+ ", xLabel=" + xLabel + ", xParameter=" + xParameter + ", xScrollbar=" + xScrollbar + ", xCategory="
				+ xCategory + ", yLabel=" + yLabel + ", yParameter=" + yParameter + ", yScrollbar=" + yScrollbar
				+ ", yCategory=" + yCategory + ", sequence=" + sequence + ", frameDetail=" + frameDetail + ", isActive="
				+ isActive + ", filterData=" + filterData + ", xFilter=" + xFilter + ", yFilter=" + yFilter
				+ ", xAxisTopValue=" + xAxisTopValue + ", yAxisTopValue=" + yAxisTopValue + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
	}

}