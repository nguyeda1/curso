const upperCaseFirstLetter = string => {
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const lowerCaseAllExceptFirstLetters = string => {
  return string.replace(/\w\S*/g, function(word) {
    return word.charAt(0) + word.slice(1).toLowerCase();
  });
};

const replaceUnderscoreForSpace = string => {
  return string.replace("_", " ");
};

const normalizeText = string => {
  return upperCaseFirstLetter(
    lowerCaseAllExceptFirstLetters(replaceUnderscoreForSpace(string))
  );
};

export default normalizeText;
