import { INTEGER_2 } from '@/utils/constant';

// 判断是否是有效值
const judgeIsNull = (val: any) => {
  if (val !== 0 && !val) {
    return '--';
  }
  if (typeof val === 'number' && Number.isNaN(val)) {
    return '--';
  }
  return val;
};

/**
 * @param value Number
 * @param format +M.N%，+：代表是否增加正号；M：固定占位；.N: 保留小数位数；%：是否增加百分号；
 */
const mathFormatHandler = function (value: any, format: string) {
  const isInvalidFormat = /^\+?M(\.[1-9])?%?$/.test(format);
  const currentNumber = value;
  if (!isInvalidFormat || judgeIsNull(currentNumber) === '--') {
    return '--';
  }
  const isDecimal = Number(
    format.indexOf('.') >= 0 &&
      format.substring(format.indexOf('.') + 1, format.indexOf('.') + INTEGER_2)
  );
  const isPercent = format.includes('%');
  const isPositive = format.includes('+');
  const currentVal = parseFloat(currentNumber);
  let resultVal: string | number = parseFloat(currentNumber);
  resultVal = isDecimal ? resultVal.toFixed(isDecimal) : resultVal;
  resultVal = isPercent ? `${resultVal}%` : resultVal;
  resultVal = isPositive && currentVal > 0 ? `+${resultVal}` : resultVal;
  return String(resultVal);
};

export {
  judgeIsNull,
  mathFormatHandler,
};
