package bdmmprime.util.priors;

import beast.core.CalculationNode;
import beast.core.Function;
import beast.core.Input;
import beast.core.parameter.RealParameter;

public class MigRateGLM extends CalculationNode implements Function {

    public Input<Function> covariateListInput = new Input<>("covariateList",
            "List of matrix of covariates, e.g. numbers of flights between different locations",
            Input.Validate.REQUIRED);

    public Input<Function> scalerParamInput = new Input<>("scalerParam",
            "Scaler parameter.", Input.Validate.REQUIRED);

    public Input<Function> indicatorParamInput = new Input<>("indicatorParam",
            "Indicator parameter.", Input.Validate.REQUIRED);

    public Input<RealParameter> globalScalerParamInput = new Input<>("globalScalerParam",
            "Global scaler parameter.", Input.Validate.OPTIONAL);

//    final public Input<Double> scaleUpperLimit = new Input<>("upper", "Upper Limit of scale factor", 100.0);
//    final public Input<Double> scaleLowerLimit = new Input<>("lower", "Lower limit of scale factor", 0.0);


//    public Input<Function> errorInput = new Input<>("errorTerm",
//            "Error term.", Input.Validate.OPTIONAL);

    Function covariateList, scalerParam, indicatorParam, globalScalerParam;
    int covariateSize;
//    private double upper, lower;

    @Override
    public void initAndValidate() {
        covariateList = covariateListInput.get();
        scalerParam = scalerParamInput.get();
        indicatorParam = indicatorParamInput.get();

        globalScalerParam = globalScalerParamInput.get();
//        errorTerm = errorInput.get();

        covariateSize = covariateList.getDimension()/(scalerParam.getDimension());

//        upper = scaleUpperLimit.get();
//        lower = scaleLowerLimit.get();
    }

//    protected boolean outsideBounds(final double value) {
//
//        return (value < lower || value > upper);
//        //return (l != null && value < l || h != null && value > h);
//    }

    @Override
    public int getDimension() {
        return covariateSize;
    }

    @Override
    public double getArrayValue(int i) {
        double lograte = 0;
        for (int j = 0; j < scalerParam.getDimension(); j++) {
            if (indicatorParam.getArrayValue(j) > 0.0) {
                lograte += scalerParam.getArrayValue(j) * covariateList.getArrayValue(j * covariateSize + i);
            }
        }

//        if (errorTerm!=null)
//            lograte += errorTerm.getArrayValue(i);

//        if (globalScalerParam.getArrayValue() * Math.exp(lograte) > 10000.0) {
//            throw new IllegalArgumentException("parameter scaled out of range");
//        }

        return globalScalerParam.getArrayValue() * Math.exp(lograte);
    }
}
