package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "GRAPH_CONFIGURATION")
public class GraphConfigurationDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GRAPH_CONFIG_ID", length = 255)
	private String graphConfigId;

	@Column(name = "USER_ID", length = 255)
	private String userId;

	@Column(name = "GRAPH_NAME", length = 255)
	private String graphName;

	@Column(name = "CHART_TYPE", length = 255)
	private String chartType;

	@Column(name = "GRID_VIEW")
	private Boolean gridView;

	@Column(name = "SHOW_LEGENDS")
	private Boolean showLegends;

	@Column(name = "X_LABEL", length = 255)
	private String xLabel;

	@Column(name = "X_PARAMETER", length = 255)
	private String xParameter;

	@Column(name = "X_SCROLLBAR")
	private Boolean xScrollbar;

	@Column(name = "X_CATEGORY", length = 255)
	private String xCategory;

	@Column(name = "X_CATEGORY_FILTER", length = 255)
	private String xFilter;
	
	@Column(name = "Y_LABEL", length = 255)
	private String yLabel;

	@Column(name = "Y_PARAMETER", length = 255)
	private String yParameter;

	@Column(name = "Y_SCROLLBAR")
	private Boolean yScrollbar;

	@Column(name = "Y_CATEGORY", length = 255)
	private String yCategory;
	
	@Column(name = "Y_CATEGORY_FILTER", length = 255)
	private String yFilter;

	@Column(name = "SEQUENCE")
	private Integer sequence;

	@Column(name = "FRAME_DETAIL", length = 255)
	private String frameDetail;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "FILTER_DATA")
	private String filterData;
	
	@Column(name = "X_AXIS_TOP_VALUE")
	private Integer xAxisTopValue;
	
	@Column(name = "Y_AXIS_TOP_VALUE")
	private Integer yAxisTopValue;
	
	@Override
	public String toString() {
		return "GraphConfigurationDo [graphConfigId=" + graphConfigId + ", userId=" + userId + ", graphName="
				+ graphName + ", chartType=" + chartType + ", gridView=" + gridView + ", showLegends=" + showLegends
				+ ", xLabel=" + xLabel + ", xParameter=" + xParameter + ", xScrollbar=" + xScrollbar + ", xCategory="
				+ xCategory + ", xFilter=" + xFilter + ", yLabel=" + yLabel + ", yParameter=" + yParameter
				+ ", yScrollbar=" + yScrollbar + ", yCategory=" + yCategory + ", yFilter=" + yFilter + ", sequence="
				+ sequence + ", frameDetail=" + frameDetail + ", isActive=" + isActive + ", filterData=" + filterData
				+ ", xAxisTopValue=" + xAxisTopValue + ", yAxisTopValue=" + yAxisTopValue + "]";
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

	public String getxFilter() {
		return xFilter;
	}

	public void setxFilter(String xFilter) {
		this.xFilter = xFilter;
	}

	public String getyFilter() {
		return yFilter;
	}

	public void setyFilter(String yFilter) {
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
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
