package pers.juumii.MindTrace.view.console;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.data.Knowledge;

/**
 * 用于保存语境信息
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContextInfo {
    private Knowledge location;
}
