package com.est.myapplication.boot;

import android.view.inputmethod.InlineSuggestionsRequest;

import java.util.ArrayList;
import java.util.List;

public class Boot {

    private final int[][] WINNING_COMBINATIONS = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 },
                                                   { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 },
                                                   { 1, 5, 9 }, { 3, 5, 7 } };

    private List<Integer> sensitiveCells = new ArrayList<>();
    private int choice;

    public int setBootChoice(List<Integer> bootChoices, List<Integer> userChoices) {
        if (checkBootWinning(bootChoices, userChoices )) return choice;
        if (checkUserWinning(bootChoices, userChoices)) return choice;

        playBestMove(bootChoices, userChoices);
        sensitiveCells.clear();

        return choice;
    }

    private boolean checkBootWinning(List<Integer> bootChoices, List<Integer> userChoices) {
        for (int[] combination : WINNING_COMBINATIONS) {
            int count = 0;
            int emptyCell = 0;
            for (int cell : combination) {
                if (bootChoices.contains(cell)) {
                    count++;
                } else if (!userChoices.contains(cell)) {
                    emptyCell = cell;
                }
            }
            if (count == 2 && emptyCell != 0) {
                setBootChoice(bootChoices, emptyCell);
                choice = emptyCell;
                System.out.println("Boot chooses " + emptyCell);
                return true;
            }
        }
        return false;
    }

    private boolean checkUserWinning(List<Integer> bootChoices, List<Integer> userChoices) {
        for (int[] combination : WINNING_COMBINATIONS) {
            int count = 0;
            int emptyCell = 0;
            for (int cell : combination) {
                if (userChoices.contains(cell)) {
                    count++;
                } else if (!bootChoices.contains(cell)) {
                    emptyCell = cell;
                }
            }
            if (count == 2 && emptyCell != 0) {
                setBootChoice(bootChoices, emptyCell);
                choice = emptyCell;
                System.out.println("Boot chooses " + emptyCell);
                return true;
            }
        }
        return false;
    }

    private void playBestMove(List<Integer> bootChoices, List<Integer> userChoices) {
        List<Integer> preferredCells = new ArrayList<>();

        if (!userChoices.contains(5) && !bootChoices.contains(5) && !sensitiveCells.contains(5))
            preferredCells.add(5);

        if (!userChoices.contains(1) && !bootChoices.contains(1) && !sensitiveCells.contains(1))
            preferredCells.add(1);

        if (!userChoices.contains(3) && !bootChoices.contains(3) && !sensitiveCells.contains(3))
            preferredCells.add(3);

        if (!userChoices.contains(7) && !bootChoices.contains(7) && !sensitiveCells.contains(7))
            preferredCells.add(7);

        if (!userChoices.contains(9) && !bootChoices.contains(9) && !sensitiveCells.contains(9))
            preferredCells.add(9);

        if (!userChoices.contains(4) && !bootChoices.contains(4) && !sensitiveCells.contains(4))
            preferredCells.add(4);

        if (!userChoices.contains(8) && !bootChoices.contains(8) && !sensitiveCells.contains(8))
            preferredCells.add(8);

        if (!userChoices.contains(6) && !bootChoices.contains(6) && !sensitiveCells.contains(6))
            preferredCells.add(6);

        if (!userChoices.contains(2) && !bootChoices.contains(2) && !sensitiveCells.contains(2))
            preferredCells.add(2);


        if (preferredCells.isEmpty())
            return;

        if (userChoices.size() == 2 && bootChoices.size() == 1) {
            int cell = bestAttackAndDefencePosition(bootChoices, userChoices);
            System.out.println("the choised cell is : " + cell);
            if (cell != -1) {
                setBootChoice(bootChoices, cell);
                choice = cell;
                System.out.println("boot defence and atack in cell :  " + cell);
                return;
            }
        }

        int defencePosition = bestDefencePosition(bootChoices, userChoices, preferredCells);

        if (defencePosition != -1 && userChoices.size() >= 2) {
            setBootChoice(bootChoices, defencePosition);
            choice = defencePosition;
            System.out.println("Boot defending on " + defencePosition);
            return;
        }

        int attackPosition = bestAttackPosition(bootChoices, userChoices, preferredCells);
        setBootChoice(bootChoices, attackPosition);
        choice = attackPosition;
        System.out.println("Boot attacking on " + attackPosition);

    }

    private int bestAttackPosition(List<Integer> bootChoices, List<Integer> userChoices, List<Integer> preferedCells) {

        for (int cell : preferedCells) {
            if (!sensitiveCells.contains(cell)) {
                List<Integer> simulatedBootChoices = new ArrayList<>();
                List<Integer> simulatedUserChoices = new ArrayList<>();

                for (Integer choice : bootChoices) {
                    simulatedBootChoices.add(choice);
                }


                simulatedBootChoices.add(cell);

                int loosingPossibilities = 0;
                int combinationCount = checkBootWinningCombination(simulatedBootChoices, userChoices);
                System.out.println("possible combinations for " + cell + " is " + combinationCount );

                // check if this step could cause any reversed attack by the user

                for (Integer otherChoice : preferedCells) {
                    if (otherChoice != cell) {
                        for (Integer choice : userChoices) {
                            simulatedUserChoices.add(choice);
                        }

                        simulatedUserChoices.add(otherChoice);
                        loosingPossibilities = checkBootLoosingCombination(simulatedBootChoices, simulatedUserChoices);
                    }
                }


                if (combinationCount > 1 && loosingPossibilities < 2) {
                    System.out.println("chooosed cell : " + cell);
                    System.out.println("combination count : " + combinationCount + " loosingPossibilities : " + loosingPossibilities);
                    return cell;
                }
            }
        }
        for (int cell : preferedCells) {
            if (!sensitiveCells.contains(cell))
                return cell;
        }

        return preferedCells.get(0);
    }

    private int bestStartingAttackPosition(List<Integer> bootChoices, List<Integer> userChoices, List<Integer> preferedCells) {
        int perfectChoice = preferedCells.get(0);
        int posiblities = 0;

        for (int cell : preferedCells) {
            List<Integer> simulatedBootChoices = new ArrayList<>();
            List<Integer> simulatedUserChoices = new ArrayList<>();


            for (Integer choice : bootChoices) {
                simulatedBootChoices.add(choice);
            }
            simulatedBootChoices.add(cell);

            int loosingPossibilities = 0;
            int combinationCount = checkBootWinningCombination(simulatedBootChoices, userChoices);
            System.out.println("possible combinations for " + cell + " is " + combinationCount );


            // check if this step could cause any reversed attack by the user
            for (Integer otherChoice : preferedCells) {
                if (otherChoice != cell) {
                    for (Integer choice : userChoices) {
                        simulatedUserChoices.add(choice);
                    }

                    simulatedUserChoices.add(otherChoice);
                    loosingPossibilities = checkBootLoosingCombination(simulatedBootChoices, simulatedUserChoices);
                }
            }



            if (combinationCount >= posiblities && !userChoices.contains(cell) && loosingPossibilities < 2) {
                posiblities = combinationCount;
                perfectChoice = cell;
                System.out.println("chooosed cell : " + cell);
            }

        }

        if (posiblities == 0)
            return -1;

        return perfectChoice;
    }



    private int bestDefencePosition(List<Integer> bootChoices, List<Integer> userChoices, List<Integer> preferedCells) {

        int defendCell = 0;

        for (Integer cell : preferedCells) {

            List<Integer> simulatedUserChoices = new ArrayList<>();

            for (Integer choice : userChoices) {
                simulatedUserChoices.add(choice);
            }

            simulatedUserChoices.add(cell);

            int combinationCount = checkBootLoosingCombination(bootChoices,simulatedUserChoices);


            if (combinationCount > 1) {
                defendCell = cell;

                if (combinationCount > 2)
                    sensitiveCells.add(cell);
            }
        }

        if (sensitiveCells.size() > 1) {
            return -1;
        }


        return defendCell;
    }

    private int bestAttackAndDefencePosition(List<Integer> bootChoices, List<Integer> userChoices) {

        List<Integer> preferedCells = new ArrayList<>();

        if (!sensitiveCells.contains(2))
            preferedCells.add(2);

        if (!sensitiveCells.contains(4))
            preferedCells.add(4);

        if (!sensitiveCells.contains(6))
            preferedCells.add(6);

        if (!sensitiveCells.contains(8))
            preferedCells.add(8);


        for (Integer cell : preferedCells) {
            if (!bootChoices.contains(cell) && !userChoices.contains(cell)) {
                int attackPosition = bestStartingAttackPosition(bootChoices, userChoices, preferedCells);
                if (attackPosition != -1) {
                    System.out.println("this is the best cell : " + attackPosition);
                    return attackPosition;
                }

            }
        }

        return -1;

    }


    private int checkBootWinningCombination(List<Integer> bootChoices, List<Integer> userChoices) {
        int possibleWinningCombination = 0;

        for (int[] combination : WINNING_COMBINATIONS) {
            int count = 0;
            int emptyCell = 0;
            for (int cell : combination) {
                if (bootChoices.contains(cell)) {
                    count++;
                } else if (!userChoices.contains(cell)) {
                    emptyCell = cell;
                }
            }

            if (count == 2 && emptyCell != 0) possibleWinningCombination++;

        }

        return possibleWinningCombination;
    }

    private int checkBootLoosingCombination(List<Integer> bootChoices, List<Integer> userChoices) {
        int possibleLoosingCombination = 0;

        for (int[] combination : WINNING_COMBINATIONS) {
            int count = 0;

            int cell1 = combination[0];
            int cell2 = combination[1];
            int cell3 = combination[2];

            if (userChoices.contains(cell1) && !bootChoices.contains(cell1))
                count++;

            if (userChoices.contains(cell2) && !bootChoices.contains(cell2))
                count++;

            if (userChoices.contains(cell3) && !bootChoices.contains(cell3))
                count++;

            if (count > 1)
                possibleLoosingCombination++;
        }

        return possibleLoosingCombination;
    }


    private void setBootChoice(List<Integer> bootChoices, int choice) {
        bootChoices.add(choice);
    }

}
