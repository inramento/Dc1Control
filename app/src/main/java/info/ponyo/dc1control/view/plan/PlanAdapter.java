package info.ponyo.dc1control.view.plan;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import info.ponyo.dc1control.R;
import info.ponyo.dc1control.base.CommonAdapter;
import info.ponyo.dc1control.base.CommonViewHolder;
import info.ponyo.dc1control.bean.Dc1Bean;
import info.ponyo.dc1control.bean.PlanBean;
import info.ponyo.dc1control.network.socket.ConnectApi;
import info.ponyo.dc1control.util.SpanUtil;

/**
 * @author zxq
 * @Date 2019/8/26.
 * @Description:
 */
public class PlanAdapter extends CommonAdapter<PlanBean> {
    private Dc1Bean dc1Bean;

    @Override
    public int initLayoutId() {
        return R.layout.item_plan;
    }

    @Override
    public void onBind(CommonViewHolder holder, int position) {
        PlanBean bean = getData().get(position);
        SwitchCompat sw = holder.getView(R.id.sw);
        sw.setOnCheckedChangeListener(null);

        String switchIndex = bean.getSwitchIndex();
        String switchName;
        switch (switchIndex) {
            case PlanBean.SWITCH_INDEX_MAIN: {
                switchName = getName(1, "总开关");
                break;
            }
            case PlanBean.SWITCH_INDEX_FIRST: {
                switchName = getName(2, "分控1");
                break;
            }
            case PlanBean.SWITCH_INDEX_SECOND: {
                switchName = getName(3, "分控2");
                break;
            }
            case PlanBean.SWITCH_INDEX_THIRD: {
                switchName = getName(4, "分控3");
                break;
            }
            default: {
                switchName = "未知";
                break;
            }
        }

        String command = TextUtils.equals(bean.getCommand(), "0") ? "关" : "开";
        SpannableStringBuilder stringBuilder = SpanUtil.build()
                .append(switchName)
                .append(" : ")
                .append(command).setForegroundColor(ContextCompat.getColor(holder.getContext(), R.color.colorPrimary))
                .create();

        holder.setText(R.id.tv_trigger_time, "触发时间  " + bean.getTriggerTime())
                .setText(R.id.tv_trigger_status, stringBuilder)
                .setText(R.id.tv_repeat, "  |  周期：" + bean.getRepeat_2showstr())
                .setChecked(R.id.sw, bean.isEnable())
                .setOnItemClickListener();

        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConnectApi.enablePlanById(bean.getId(), isChecked);
        });
    }

    private String getName(int pos, String defaultName) {
        if (dc1Bean != null && dc1Bean.getNames() != null && dc1Bean.getNames().size() == 5) {
            defaultName = dc1Bean.getNames().get(pos);
        }
        return defaultName;
    }

    public PlanAdapter setDc1Bean(Dc1Bean dc1Bean) {
        this.dc1Bean = dc1Bean;
        return this;
    }
}
